package com.sycamore.ticketing_system.ticketService.common.enums;

/**
 * Redis Key 定义常量类
 *
 *
 */
public final class RedisKeyConstant {
    
    public static final String PREFIX = "ticket-service:";
    /**
     * 列车基本信息，Key Prefix + 列车ID
     */
    public static final String TRAIN_INFO = PREFIX + "train_info:";

    /**
     * 地区与站点映射查询
     */
    public static final String REGION_TRAIN_STATION_MAPPING = PREFIX + "region_train_station_mapping";

    /**
     * 站点查询分布式锁 Key
     */
    public static final String LOCK_REGION_TRAIN_STATION_MAPPING = PREFIX + "lock:region_train_station_mapping";

    /**
     * 站点查询，Key Prefix + 起始城市_终点城市_日期
     */
    public static final String REGION_TRAIN_STATION = PREFIX + "region_train_station:%s_%s";

    /**
     * 站点查询分布式锁 Key
     */
    public static final String LOCK_REGION_TRAIN_STATION = PREFIX + "lock:region_train_station";

    /**
     * 列车站点座位价格查询，Key Prefix + 列车ID_起始城市_终点城市
     */
    public static final String TRAIN_STATION_PRICE = PREFIX + "train_station_price:%s_%s_%s";

    /**
     * 列车站点座位价格查询分布式锁 Key
     */
    public static final String LOCK_TRAIN_STATION_PRICE = PREFIX + "lock:train_station_price";

    /**
     * 地区以及车站查询，Key Prefix + ( 车站名称 or 查询方式 )
     */
    public static final String REGION_STATION = PREFIX + "region-station:";

    /**
     * 站点余票查询，Key Prefix + 列车ID_起始站点_终点
     */
    public static final String TRAIN_STATION_REMAINING_TICKET = PREFIX + "train_station_remaining_ticket:";

    /**
     * 列车车厢查询，Key Prefix + 列车ID
     */
    public static final String TRAIN_CARRIAGE = PREFIX + "train_carriage:";

    /**
     * 车厢余票查询，Key Prefix + 列车ID_起始站点_终点
     */
    public static final String TRAIN_STATION_CARRIAGE_REMAINING_TICKET = PREFIX + "train_station_carriage_remaining_ticket:";

    /**
     * 站点详细信息查询，Key Prefix + 列车ID_起始站点_终点
     */
    public static final String TRAIN_STATION_DETAIL = PREFIX + "train_station_detail:";

    /**
     * 列车路线信息查询，Key Prefix + 列车ID
     */
    public static final String TRAIN_STATION_STOPOVER_DETAIL = PREFIX + "train_station_stopover_detail:";

    /**
     * 列车站点缓存
     */
    public static final String STATION_ALL = PREFIX + "all_station";

    /**
     * 列车车厢状态， Key Prefix + 列车 ID + 起始站点 + 目的站点 + 车厢编号
     */
    public static final String TRAIN_CARRIAGE_SEAT_STATUS = PREFIX + "train_carriage_seat_status:";

    /**
     * 用户购票分布式锁 Key
     */
    public static final String LOCK_PURCHASE_TICKETS = "${unique-name:}" + PREFIX + "lock:purchase_tickets_%s";

    /**
     * 用户购票分布式锁 Key v2
     */
    public static final String LOCK_PURCHASE_TICKETS_V2 = "${unique-name:}" + PREFIX + "lock:purchase_tickets_%s_%d";

    /**
     * 获取全部地点集合 Key
     */
    public static final String QUERY_ALL_REGION_LIST = PREFIX + "query_all_region_list";

    /**
     * 列车购买令牌桶，Key Prefix + 列车ID
     */
    public static final String TICKET_AVAILABILITY_TOKEN_BUCKET = PREFIX + "ticket_availability_token_bucket:";

    /**
     * 获取全部地点集合分布式锁 Key
     */
    public static final String LOCK_QUERY_ALL_REGION_LIST = PREFIX + "lock:query_all_region_list";

    /**
     * 获取列车车厢数量集合分布式锁 Key
     */
    public static final String LOCK_QUERY_CARRIAGE_NUMBER_LIST = PREFIX + "lock:query_carriage_number_list_%s";

    /**
     * 获取地区以及站点集合分布式锁 Key
     */
    public static final String LOCK_QUERY_REGION_STATION_LIST = PREFIX + "lock:query_region_station_list_%s";

    /**
     * 获取相邻座位余票分布式锁 Key
     */
    public static final String LOCK_SAFE_LOAD_SEAT_MARGIN_GET = PREFIX + "lock:safe_load_seat_margin_%s";

    /**
     * 列车购买令牌桶加载数据 Key
     */
    public static final String LOCK_TICKET_AVAILABILITY_TOKEN_BUCKET = PREFIX + "lock:ticket_availability_token_bucket:%s";
}
