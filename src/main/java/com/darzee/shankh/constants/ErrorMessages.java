package com.darzee.shankh.constants;

public class ErrorMessages {

    public static String USER_ALREADY_EXIST = "You already have an account. Please login";
    public static String MEASUREMENT_CUSTOMER_ID_ERROR = "Customer id is invalid";
    public static String MEASUREMENT_DETAIL_NOT_FOUND_ERROR = "Measurement details not found";
    public static String INVALID_PARAMS_ERROR = "Either send measurement rev id/ order item id or (outfit type and customer id) is required";
    public static String OUTFIT_TYPE_NOT_SUPPORTED_ERROR = "Outfit Type not supported";
    public static String INVALID_MEASUREMENT_SCALE_ERROR = "Invalid Measurement Scale";
    public static String  INVALID_ORDER_ITEM_ID_ERROR ="Incorrect order item id";
    public static String INVALID_ORDER_ID_ERROR="Either Order ID or Boutique ID is mandatory";
    public static String INVALID_ITEM_ID ="Item ID is invalid";
    public static String INVALID_OUTFIT_TYPE_ERROR="Invalid outfit type ";
    public static String BOUTIQUE_LEDGER_NOT_FOUND_ERROR = "Boutique Ledger for this id doesn't exist";
    public static String BOUTIQUE_LEDGER_NOT_EXIST_ERROR_MSG = "Boutique ledger doesn't exist for boutique ";
    public static String INVALID_BOUTIQUE_ID_ERROR   ="Invalid boutique id";
    public static String INVALID_OUTFIT_SIDE_ERROR="Invalid outfit side";
    public static String MEASUREMENT_PARAM_NAME_MISSING                                        ="Measurement param name is missing";
    public static String FILE_UPLOAD_ERROR ="File upload failed wtith exception {}";
    public static String FILE_UPLOAD_ERROR_MSG="File upload failed with exception ";
    public static String BOUTIQUE_NOT_ENROLLED_ERROR="This boutique is not enrolled with us";
    public static String CUSTOMER_NOT_REGISTERED_ERROR = "Sorry! Customer with this ID is not registered with us";
    public static String CUSTOMER_ALREADY_REGISTERED_ERROR = "Sorry! Customer with same phone number is already registered with this boutique";
    public static String INVALID_CUSTOMER_ID= "Invalid Customer id";
    public static String INVALID_TAILOR_ID ="Invalid tailor Id";
    public static String INVALID_REPORTING_DATE  ="Reporting of future dates is not supported";
    public static String ITEM_PDF_GENERATION_ERROR ="Error occurred while generating item PDF";
    public static String INVALID_ORDER_ITEM ="Invalid Order Item Id";
    public static String INVALID_CUSTOMER_ID_OR_BOUTIQUE_ID_ERROR  ="Invalid customer id or boutique id. Boutique ID and customer ID are mandatory fields. Some mandatory fields in order are missing.";
   public static String INVALID_BOUTIQUE_ID_AND_CUSTOMER_ID =" Boutique ID and customer ID are mandatory fields. Some mandatory fields in order are missing.";
    public static String INVALID_PRICE_BREAKUP ="Items' Price Breakups are not summing up to total amount.{0} and price break up sum Amount recieved is greater than pending order amount. Amount Received : {1}";
    public static String INVALID_AMOUNT_RECEIVED ="Amount recieved is greater than pending order amount. Amount Received : {0} . Pending Amount : {1}";
    public static String PORTFOLIO_NOT_FOUND = "Portfolio not found";
    public static String ORDER_NOT_FOUND="Order not found";     
    public static String INTERNAL_SERVER_ERROR="Internal Server Error";
     public static String REFUND_GREATER_THAN_ADVANCE ="Refund cannot be greater than advance received";

    public static String REFUND_GREATER_THAN_ORDER_CHANGE_MSG ="Amount refund could not be greater than order amount change. Order Amount changed by {0} and we cannot refund {1}";
    public static String INVALID_PRICE_BREAKUP_OR_TOTAL_ORDER_AMOUNT ="Either price break up or total order amount is incorrect";
    public static String INVALID_ORDER_STATUS_AND_ORDER_ITEM_STATUS = "Either order status or item status is necessary to get orders";
    public static String INVALID_ADVANCE_RECEIVED ="Advance Received could not be greater than Total Order Amount";
   public static String INVALID_ORDER ="Invalid order ID";
   public static String INVALID_OUTFIT_ID="Invalid Outfit Id";
   public static String INVALID_STITCH_OPTION="Some Stitch Option ID is invalid";
   
}
