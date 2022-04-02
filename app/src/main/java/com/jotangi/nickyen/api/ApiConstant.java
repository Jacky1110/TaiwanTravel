package com.jotangi.nickyen.api;

public class ApiConstant
{

    // test url
//    public static final String API_URL = "http://211.20.185.2/tours/api/";
//    public static final String API_IMAGE = "http://211.20.185.2/tours/";
    // Mall test
    public static final String API_MALL_URL ="https://tripspottest.jotangi.net/tours/api/";
    public static final String API_MALL_IMAGE ="https://tripspottest.jotangi.net/tours/";
    // formal url
    public static final String API_URL = "https://tripspot.jotangi.net/api/";
    public static final String API_IMAGE = "https://tripspot.jotangi.net/";

    /**
     * 首頁相關
     */
    //Banner
    static final String BannerList = "banner_list.php";
    //News
    static final String NewList = "news_list.php";
    //推播列表
    static final String Push_List = "pushmsg_list.php";
    //發送給會員推播
    static final String FCM_TO_MEMBER = "fcm_tomember.php";
    //發送給店家推播
    static final String FCM_TO_STORE = "fcm_tostore.php";
    // 判斷是否為紅利店家
    static final String IS_BONUS_STORE = "is_bonusstore.php";

    /**
     * 會員相關
     */
    //登入
    static final String LOGIN = "user_login.php";
    //會員資料
    static final String MemberInfo = "member_info1.php";
    //註冊
    static final String Register = "user_register.php";
    //登出
    static final String LogOut = "user_logut.php";
    //重設密碼的驗證碼
    static final String UserCode = "user_code.php";
    //重設密碼
    static final String UserResetpwd = "user_resetpwd.php";
    //更改密碼
    static final String UserChangepwd = "user_changepwd.php";
    //更改會員資料
    static final String UserEdit = "user_edit.php";
    //獲取會員消費記錄
    static final String MemberOrderList = "order_list.php";
    //獲取會員消費記錄內頁
    static final String MemberOrderInfo = "order_info.php";
    //發送手機驗證碼
    static final String VerifyCode = "verify_code.php";
    //檢查手機驗證碼
    static final String CheckVerify = "check_verify.php";
    //會員照片上傳
    static final String UserUploadPic = "user_uploadpic.php";
    //常見問題
    static final String QUESTION_LIST = "question_list.php";

    /**
     * 附近店家
     */
    //商家列表
    static final String StoreList = "store_list.php";
    //商家資訊
    public static final String StoreInfo = "store_info.php";
    //商家資訊
    static final String GetMemberCard = "membercard_list.php";

    /**
     * 優惠券
     */
    //會員好康推薦的優惠券清單
    static final String CouponList = "coupon_list.php";
    //會員好康推薦領用的
    static final String GetCoupon = "get_coupon.php";
    //會員已領用過的優惠券清單
    static final String MyCouponList = "mycoupon_list.php";
    //店長檢查優惠券狀態
    static final String CheckCoupon = "check_coupon.php";
    //店長核銷非折抵現金券票券
    static final String ApplyCoupon = "apply_coupon.php";

    /**
     * 入會禮相關
     */
    //確認是否回該店家會員
    static final String IsMemberStatus = "is_storemember.php";
    //加入會員
    static final String AddMemberCard = "add_membercard.php";

    /**
     * 點數相關
     */
    //會員點數清單
    static final String BonusList = "bonus_list.php";

    /**
     * 店長相關
     */
    //店長檢查點數狀態
    static final String CheckBonus = "check_bonus.php";
    //店長結帳核銷用
    static final String AddOrder = "add_order.php";
    //店長交易紀錄
    static final String MerchOrderList = "storeorder_list.php";
    //店長交易紀錄明細
    static final String MerchOrderInfo = "storeorder_info.php";
    //店長的美容美髮預約記錄
    static final String StoreBookingList = "store_bookinglist.php";
    //店長的美容美髮預約記錄明細
    static final String StoreBookingInfo = "store_bookinginfo.php";
    //店長的查詢設計師
    static final String StoreHairStyList = "store_hairstylist.php";
    // 店長產業體驗紀錄
    static final String StoreClassBookingList = "store_classbookinglist.php";
    // 店長產業體驗紀錄訂單明細
    static final String StoreClassBookingInfo = "store_classbookinginfo.php";
    // 店長美容美髮更新預約狀態
    static final String STORE_BOOKING_UPDATE = "storebooking_update.php";
    // 店長產業體驗更新預約狀態
    static final String STORE_CLASS_BOOKING_UPDATE = "storeclassbooking_update.php";
    // 店長對帳
    static final String PROFIT_INFO = "profit_info.php";

    /**
     * 會員結帳相關
     */
    //美容美髮結帳用
    static final String MemberAddOrder = "member_addorder.php";
    // 產業體驗結帳用
    static final String MemberAddClassOrder = "member_addclassorder.php";

    /**
     * AR相關
     */
    // For北埔專用
    static final String arStore = "ar_store.php";
    // 送出店家與使用者的經緯度來計算距離
    static final String getDistance = "get_distance.php";
    // AR點位內文
    static final String arInfo = "ar_info.php";
    // AR點位清單
    static final String arList = "ar_list.php";
    // 針對AR的核銷優惠券
    static final String applyCoupon2 = "apply_coupon2.php";
    // AR的優惠券說明
    static final String myCouponList2 = "mycoupon_list2.php";
    // 針對AR所領取的優惠券
    static final String getCoupon2 = "get_coupon2.php";

    /**
     * 美容美髮相關
     */
    //預約檢查黑名單
    static final String IsStoreBlackList = "is_storeblacklist.php";
    //查詢設計師
    static final String GetHairstylist = "get_hairstylist.php";
    //選擇服務項目
    static final String GetHairService = "get_hairservice.php";
    //預約日期
    static final String GetWorkingDay = "get_workingday.php";
    //查詢指定日期預約訂單
    static final String IsBookingDay = "is_bookingday.php";
    //新增美容美髮預約訂單
    static final String AddBooking = "add_booking.php";
    //會員專區美容美法預約紀錄
    static final String BookingList = "booking_list.php";
    //會員專區美容美髮訂單資訊
    static final String BookingInfo = "booking_info.php";
    //會員專區取消訂單
    static final String BookingCancel = "booking_cancel.php";

    /**
     * 問券
     */
    //取得店家問券資料
    static final String QUESTIONNAIRE_LIST = "questionnaire_list.php";
    //送出問券答案
    static final String SEND_QUESTIONNAIRE = "send_questionnaire.php";

    /**
     * 產業體驗
     */
    //產業體驗
    static final String CLASS_LIST = "class_list.php";
    //體驗課程項目
    static final String PROGRAM_LIST = "program_list.php";
    //產業體驗預約日期
    static final String GET_CLASS_WORKING_DAY = "get_classworkingday.php";
    //查詢產業體驗指定預約日期
    static final String IS_CLASS_BOOKING_DAY = "is_classbookingday.php";
    //送出訂單
    static final String ADD_CLASS_BOOKING_DAY = "add_classbooking.php";
    //會員查詢體驗紀錄
    static final String CLASS_BOOKING_LIST = "classbooking_list.php";
    //體驗記錄訂單資訊
    static final String CLASS_BOOKING_INFO = "classbooking_info.php";
    // 會員專區取消體驗訂單
    static final String CLASS_BOOKING_CANCEL = "classbooking_cancel.php";

    /**
     * 星座測驗
     */
    //送出測驗
    static final String  SAVE_CONSTELLATION = "save_constellation.php";

    /**
     * 商城
     */
    // 商城標籤列表
    static final String  PRODUCT_TYPE = "product_type.php";
    // 商品列表
    static final String  PRODUCT_LIST = "product_list.php";
    // 商品資訊
    static final String  PRODUCT_INFO = "product_info.php";
    // 加入購物車
    static final String  ADD_SHOPPING_CART = "add_shoppingcart.php";
    // 購物車內商品列表
    static final String  SHOPPING_CART_LIST = "shoppingcart_list.php";
    // 修改購物車數量
    static final String  EDIT_SHOPPING_CART = "edit_shoppingcart.php";
    // 刪除購物車內某項商品
    static final String  DEL_SHOPPING_CART = "del_shoppingcart.php";
    // 購物車商品總數
    static final String  SHOPPING_CART_COUNT = "shoppingcart_count.php";
    // 清空購物車內商品
    static final String  CLEAR_SHOPPING_CART = "clear_shoppingcart.php";
    // 新增商城訂單
    static final String  ADD_ECORDER = "add_ecorder.php";
    // 商城訂單紀錄
    static final String  ECORDER_LIST = "ecorder_list.php";
    // 商城訂單明細
    static final String  ECORDER_INFO = "ecorder_info.php";
}
