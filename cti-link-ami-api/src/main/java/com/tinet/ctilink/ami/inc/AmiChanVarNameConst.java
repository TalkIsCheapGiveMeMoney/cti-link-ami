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
    public static final String CHANNEL_CNO = "channel_cno";
    
    public static final String CDR_HOTLINE = "cdr_hotline";
    public static final String CDR_ENTERPRISE_ID = "cdr_enterprise_id";
    public static final String CDR_MAIN_UNIQUE_ID = "cdr_main_unique_id";
    public static final	String UNIQUEID = "UNIQUEID";
//    public static final	String BRIDGEPEER = "BRIDGEPEER";
    public static final	String LINKEDID = "CHANNEL(linkedid)";
    public static final String CDR_START_TIME = "cdr_start_time";
    public static final String CDR_DETAIL_CNO = "cdr_detail_cno";
    public static final String CDR_DETAIL_CALL_TYPE = "cdr_detail_call_type";
    public static final String CDR_DETAIL_GW_IP = "cdr_detail_gw_ip";
    public static final String CDR_GW_IP = "cdr_gw_ip";
    public static final String CDR_CUSTOMER_NUMBER = "cdr_customer_number";
    public static final String CDR_CUSTOMER_NUMBER_TYPE = "cdr_customer_number_type";
    public static final String CDR_CUSTOMER_AREA_CODE = "cdr_customer_area_code";
    public static final String CDR_NUMBER_TRUNK = "cdr_number_trunk";
    public static final String CDR_CALL_TYPE = "cdr_call_type";
    public static final String CDR_CLIENT_NUMBER = "cdr_client_number";
    public static final String CDR_EXTEN = "cdr_exten";
    public static final String CDR_STATUS = "cdr_status";
    public static final String CDR_BRIDGED_CNO = "cdr_bridged_cno";
    public static final String CDR_TASK_ID = "cdr_task_id";
    public static final String CDR_TASK_INVENTORY_ID = "cdr_task_inventory_id";
    public static final String RECORD_FILE = "record_file";
    public static final String CDR_RECORD_FILE = "cdr_record_file";
    public static final String CDR_IVR_ID = "cdr_ivr_id";
    public static final String CDR_ORDER_CALL_BACK = "orderCallBackId";

    public static final String CUR_NODE = "cur_node";
    public static final String CUR_NODE_ACTION = "cur_node_action";

    public static final String CALL_POWER = "call_power";

    public static final String PREVIEW_OUTCALL_LEFT_CLID = "preview_outcall_left_clid";    
    
    
    
    public static final String CDR_QUEUE_NAME = "cdr_queue_name"; 
    public static final String CUR_QUEUE = "cur_queue";
    public static final String ENTERPRISE_ID = "enterprise_id";
    
    public static final String CONSULT_CANCEL = "consult_cancel";
    public static final String CONSULT_CANCEL_UNCONSULT_VALUE ="10001";
    public static final String CONSULT_THREEWAY_CHAN = "consult_threeway_chan";
	public static final String DIRECT_CALL_READ_STATUS = "DIRECT_CALL_READ_STATUS";
	public static final String DIRECT_CALL_READ_DONE = "DIRECT_CALL_READ_DONE";
	public static final String CDR_FORCE_DISCONNECT = "cdr_force_disconnect";
	/**通道变量:is_investigation 前台满意度调查时会设置这个变量*/
	public static final String IS_INVESTIGATION = "is_investigation";
	/** 号码状态识别功能是否开启 **/
	public static final String IS_TSI = "is_tel_status_identification";
	/**通道变量:is_ib_record */
	public static final String IS_IB_RECORD = "is_ib_record";
	
	/**通道变量:is_ob_record  外呼是否录音 0--不录音 ，1--录音 */
	public static final String IS_OB_RECORD = "is_ob_record";
	
	/**通道变量:is_investigation_auto 是否自动满意度调查*/
	public static final String IS_INVESTIGATION_AUTO = "is_investigation_auto";
	
	public static final String DIAL_TIMEOUT = "dial_timeout";
	public static final String PRE_DIAL_RUN = "predialrun";
}
