package com.tinet.ctilink.ami.inc;

public class AmiChanVarNameConst {    
    
    //通道变量定义
    
    public static final String TRANSFEE_CNO = "transfee_cno";
    public static final String TRANSFER_CHANNEL = "transfer_channel";
    public static final String DISCONNECT_CHAN = "disconnect_chan";
    public static final String SPY_CHAN = "spy_chan";
    public static final String SPYER_CNO = "spyer_cno";
    public static final String SPIED_CNO = "spied_cno";
    public static final String SPY_OBJECT = "spy_object";
    public static final String OBJECT_TYPE = "object_type";
    public static final String WHISPER_CHAN = "whisper_chan";
    public static final String WHISPER_CNO = "whisper_cno";
    public static final String WHISPERED_CNO = "whispered_cno";
    public static final String WHISPER_OBJECT = "whisper_object";;
    public static final String THREEWAY_CHAN = "threeway_chan";
    public static final String THREEWAYER_CNO = "threewayer_cno";
    public static final String THREEWAYED_CNO = "threewayed_cno";
    public static final String THREEWAY_OBJECT = "threeway_object";;
    public static final String BARGE_CHAN = "barge_chan";
    public static final String BARGED_CNO = "barged_cno";
    public static final String BARGER_CNO = "barger_cno";
    public static final String BARGE_OBJECT = "barge_object";
    public static final String BARGER_INTERFACE = "barger_interface";
    public static final String PICKUP_CHAN = "pickup_chan";
    public static final String PICKUPER_CNO = "pickuper_cno";
    public static final String PICKUPER_INTERFACE = "pickuper_interface";
    public static final String MAIN_CHANNEL = "main_channel";
    public static final	String UNIQUEID = "UNIQUEID";
    public static final	String LINKEDID = "CHANNEL(linkedid)";
    public static final String PREVIEW_OUTCALL_LEFT_CLID = "preview_outcall_left_clid";   
    public static final String CUR_QNO = "cur_qno";
    public static final String CONSULT_CANCEL = "consult_cancel";
    public static final String CONSULT_CANCEL_UNCONSULT_VALUE ="10001";
    public static final String CONSULT_THREEWAY_CHAN = "consult_threeway_chan";
	public static final String DIRECT_CALL_READ_STATUS = "DIRECT_CALL_READ_STATUS";
	public static final String DIRECT_CALL_READ_DONE = "DIRECT_CALL_READ_DONE";
	/**通道变量:is_investigation 前台满意度调查时会设置这个变量*/
	public static final String IS_INVESTIGATION = "is_investigation";
	/**通道变量:is_investigation_auto 是否自动满意度调查*/
	public static final String IS_INVESTIGATION_AUTO = "is_investigation_auto";
	
	public static final String DIAL_TIMEOUT = "dial_timeout";
	public static final String PRE_DIAL_RUN = "predialrun";
	public static final String NUMBER_TRUNK_AREA_CODE = "number_trunk_area_code";
    
    /**通道变量:is_ib_record */
    public static final String IS_RECORD = "is_record";

    /**通道变量:is_ob_record  外呼是否录音 0--不录音 ，1--录音 */
    public static final String RECORD_SCOPE = "record_scope";

    /** 号码状态识别功能是否开启 **/
    public static final String IS_CRBT_OPEN = "is_crbt_open";

    /** 录音企业定制 **/
    public static final String RECORD_FILE_USERFIELD = "record_file_userfield";
    /** 录音企业定制 **/
    public static final String MONITOR_TYPE = "monitor_type";
    /** 录音企业定制 **/
    public static final String MP3_RATIO = "mp3_ratio";
    
    /** 通道变量:enterprise_status 企业目前业务状态 */
    public static final String INBOUND_CALL_LIMIT = "inboundCallLimit";

    /** 通道变量:enterprise_status 企业目前业务状态 */
    public static final String ENTERPRISE_STATUS = "enterprise_status";

    /** 通道变量:valid_ivr ivr是否有效 */
    public static final String VALID_IVR = "valid_ivr";

    /** 通道变量:ivr_id */
    public static final String IVR_ID = "ivr_id";
    public static final String ENTERPRISE_SETTING_NAME_CALL_LIMIT_IB = "enterprise_call_limit_ib";
    /** 通道变量:ivr_router_type 路由类型 */
    public static final String IVR_ROUTER_TYPE = "ivr_router_type";

    /** 通道变量:ivr_router_property 路由规则转移的电话 */
    public static final String IVR_ROUTER_TEL = "ivr_router_tel";
    /** 通道变量:ivr_router_property 路由规则转移的分机 */
    public static final String IVR_ROUTER_EXTEN = "ivr_router_exten";

    /** 通道变量:is_ib_record */
    public static final String IS_IB_RECORD = "is_ib_record";

    /** 通道变量:is_ob_record 外呼是否录音 0--不录音 ，1--录音 */
    public static final String IS_OB_RECORD = "is_ob_record";

    /** 通道变量:is_restrict_check 是否设置了黑白名单 */
    public static final String IS_RESTRICT_CHECK = "is_restrict_check";
    /** 通道变量:is_restrict_tel 是否在黑名单/不在白名单中 */
    public static final String IS_RESTRICT_TEL = "is_restrict_tel";
    /** 号码状态识别功能是否开启 **/
    public static final String IS_TSI = "is_tel_status_identification";
    /** 通道变量:is_remember_call 是否开通主叫记忆功能 */
    public static final String IS_REMEMBER_CALL = "is_remember_call";
    public static final String WEBCALL_TEL = "webcall_tel";
    public static final String IS_AMD_ON = "is_amd_on";
    public static final String SUBTEL = "subtel";
    /** 通道变量:queue_remember_member 上次在此队列中接听这个号码的座席号 */
    public static final String QUEUE_REMEMBER_MEMBER = "queue_remember_member";
    /** 通道变量:consulter_cno 咨询发起者的座席号 */
    public static final String CONSULTER_CNO = "consulter_cno";
    /** 通道变量:consulter_cno 被咨询的座席号 */
    public static final String CONSULTEE_CNO = "consultee_cno";
    /** 通道变量:consulter_cno 转移发起者的座席号 */
    public static final String TRANSFER_CNO = "transfer_cno";
    /** 通道变量:consulter_cno 被转移的座席号 */
    public static final String CUR_NODE = "cur_node";
    public static final String CUR_NODE_ACTION = "cur_node_action";
    public static final String CALL_POWER = "call_power";
    /** 内部呼叫座席号 */
    public static final String PREVIEW_OUTCALL_INTERNAL_CALL_CNO = "preview_outcall_internal_call_cno";    
    public static final String TEL_NUMBER_TYPE = "tel_number_type";
    public static final String TEL_NUMBER_AREA_CODE = "tel_number_area_code";
    public static final String TEL_NUMBER = "tel_number";
    
    /* ======================================================================*/
    public static final String HANGUP_HANDLER_PUSH = "CHANNEL(hangup_handler_push)";
    public static final String CDR_HOTLINE = "cdr_hotline";
    public static final String CDR_ENTERPRISE_ID = "cdr_enterprise_id";
    public static final String CDR_CNO = "cdr_cno";
    public static final String CDR_MAIN_UNIQUE_ID = "cdr_main_unique_id";
    public static final String CDR_START_TIME = "cdr_start_time";
    public static final String CDR_DETAIL_CALL_TYPE = "cdr_detail_call_type";
    public static final String CDR_DETAIL_GW_IP = "cdr_detail_gw_ip";
    public static final String CDR_GW_IP = "cdr_gw_ip";
    public static final String CDR_CUSTOMER_NUMBER = "cdr_customer_number";
    public static final String CDR_CUSTOMER_NUMBER_TYPE = "cdr_customer_number_type";
    public static final String CDR_CUSTOMER_AREA_CODE = "cdr_customer_area_code";
    public static final String CDR_NUMBER_TRUNK = "cdr_number_trunk";
    public static final String CDR_CALL_TYPE = "cdr_call_type";
    public static final String CDR_CALLEE_NUMBER = "cdr_callee_number";
    public static final String CDR_EXTEN = "cdr_exten";
    public static final String CDR_STATUS = "cdr_status";

    public static final String CDR_RECORD_FILE = "cdr_record_file";
    public static final String CDR_IVR_ID = "cdr_ivr_id";
    public static final String CDR_FORCE_DISCONNECT = "cdr_force_disconnect";
}
