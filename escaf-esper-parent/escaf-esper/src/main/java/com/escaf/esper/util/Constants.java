package com.escaf.esper.util;

public abstract class Constants
{

	public static final String BOOTSTRAP_CONFIG = "esper-config.properties";

	public static final String ADD_EVENT_OPERATION = "_def_addEvent";
	
	public static final String ESCAF_ESPER_PLUGINS="escaf.esper.plugins";
	
	public static final String ESPER_PROVIDER_URI="escaf.esper.provider.uri";

	/** 向Esper引擎中添加EPL事件的名称 */
	public static final String ADD_EVENT_NAME = "eventName";

	/** 添加的EPL事件对应的EPL语句 */
	public static final String EPL_CONTENT = "eplContent";

	/** 对应EPL事件的数据结构描述 */
	public static final String ADD_EVENT_DEFINE = "eventDefine";
	
	/** 对应EPL事件的数据结构描述 */
	public static final String ADD_EVENT_STMT_LISTENER = "eventListener";

	public static final String ADD_EVENT_STMT_LISTENER_JAR = "jar";

	public static final String ADD_EVENT_STMT_LISTENER_CLASS = "class";
	
	/**需要添加的EPL事件对应的事件类型是Map还是Class<?>的类型*/
	public static final String ADD_EVENT_TYPE="eventClassType";
	
	public static final String ADD_EVENT_CLASS_NAME="eventClassName";
	
	public static final String ADD_EVENT_CLASS_CONTENT="eventClassContent";

}
