package com.tinet.ctilink.ami.online;

import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.inc.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 在线座席管理服务
 * 
 * @author tianzp
 */
@Component
public class CtiAgentService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisService redisService;

	private HashOperations<String, String, CtiAgent> ctiAgentOperation;
	private HashOperations<String, String, Integer> integerOperation;

	/**
	 * 获取在线座席
	 * 
	 * @param cid
	 * @return the CtiAgent
	 */
	public CtiAgent get(String cid) {
		return ctiAgentOperations().get(Const.REDIS_CTIAGENT_ONLINE, cid);
	}

	/**
	 * 批量获取多个在线座席
	 * 
	 * @param cids
	 * @return
	 */
	public List<CtiAgent> gets(List<String> cids) {
		return ctiAgentOperations().multiGet(Const.REDIS_CTIAGENT_ONLINE, cids);
	}

	/**
	 * 根据通道名称获取在线座席
	 * 
	 * @param channel
	 * @return
	 */
	public CtiAgent getByChannel(String channel) {
		String cid = channel.substring(channel.indexOf('/') + 1, channel.lastIndexOf('-'));
		return get(cid);
	}

	/**
	 * 增加或更新在线座席
	 * 
	 * @param ctiAgent
	 * @return true/fasle
	 */
	public void set(CtiAgent ctiAgent) {
		// 如果用户登录，则增加所属企业的在线座席数（用于判断是否超过并发限制）
		if (!ctiAgentOperations().hasKey(Const.REDIS_CTIAGENT_ONLINE, ctiAgent.getCid())) {
			integerOperations().increment(Const.REDIS_CTIAGENT_COUNT, String.valueOf(ctiAgent.getEnterpriseId()), 1);
		}

		// 增加或更新在线座席
		ctiAgentOperations().put(Const.REDIS_CTIAGENT_ONLINE, ctiAgent.getCid(), ctiAgent);

		// 如果是绑定分机或软电话，如 SIP/3000072-8000-000001
		if (ctiAgent.getBindType() == Const.BIND_TYPE_EXTEN || ctiAgent.getBindType() == Const.BIND_TYPE_SOFT_PHONE) {
			ctiAgentOperations().put(Const.REDIS_CTIAGENT_ONLINE, ctiAgent.getEnterpriseId() + "-" + ctiAgent.getTel(),
					ctiAgent);
		}

		// 记录置忙状态
		int pause = ctiAgent.getLoginStatus().equals(CtiAgent.PAUSE) ? 1 : 0;
		integerOperations().put(Const.REDIS_CTIAGENT_PAUSE, ctiAgent.getCid(), pause);
	}

	/**
	 * 删除在线座席
	 * 
	 * @param ctiAgent
	 * @return
	 */
	public void remove(CtiAgent ctiAgent) {
		if (ctiAgentOperations().hasKey(Const.REDIS_CTIAGENT_ONLINE, ctiAgent.getCid())) {
			// 减少所属企业的在线座席数
			integerOperations().increment(Const.REDIS_CTIAGENT_COUNT, String.valueOf(ctiAgent.getEnterpriseId()), -1);

			// 删除在线座席
			ctiAgentOperations().delete(Const.REDIS_CTIAGENT_ONLINE, ctiAgent.getCid());

			// 如果是绑定分机或软电话，如 SIP/3000072-8000-000001
			if (ctiAgent.getBindType() == Const.BIND_TYPE_EXTEN
					|| ctiAgent.getBindType() == Const.BIND_TYPE_SOFT_PHONE) {
				ctiAgentOperations().delete(Const.REDIS_CTIAGENT_ONLINE,
						ctiAgent.getEnterpriseId() + "-" + ctiAgent.getTel());
			}

			// 删除置忙状态
			integerOperations().delete(Const.REDIS_CTIAGENT_PAUSE, ctiAgent.getCid());
		}
	}

	/**
	 * 获取一个企业的在线座席数
	 * 
	 * @param enterpriseId 企业Id
	 * @return
	 */
	public Integer count(Integer enterpriseId) {
		Integer result = integerOperations().get(Const.REDIS_CTIAGENT_COUNT, String.valueOf(enterpriseId));
		if (result == null) {
			return 0;
		}
		
		return result;
	}

	/**
	 * 根据cid列表批量检查是否置忙
	 * 
	 * @param cids cid列表
	 * @return 每个cid的置忙状态，1为置忙，返回的顺序与cids参数相同
	 */
	public List<Integer> checkPaused(List<String> cids) {
		return integerOperations().multiGet(Const.REDIS_CTIAGENT_PAUSE, cids);
	}

	/**
	 * 在线座席的Redis操作对象
	 * 
	 * @return
	 */
	private HashOperations<String, String, CtiAgent> ctiAgentOperations() {
		if (this.ctiAgentOperation == null) {
//			this.ctiAgentOperation = redisService.opsForHash();
		}

		return this.ctiAgentOperation;
	}

	/**
	 * 整形数字操作的Redis操作对象
	 * 
	 * @return
	 */
	private HashOperations<String, String, Integer> integerOperations() {
		if (this.integerOperation == null) {
//			this.integerOperation = redisService.opsForHash();
		}

		return this.integerOperation;
	}

}
