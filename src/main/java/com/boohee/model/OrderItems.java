package com.boohee.model;

public class OrderItems extends ModelBase {
    public static final String REFUND_ACCEPTED  = "accepted";
    public static final String REFUND_CAN       = "can_be_refund";
    public static final String REFUND_CANCELED  = "canceled";
    public static final String REFUND_FINISHED  = "finished";
    public static final String REFUND_FORBIDDEN = "forbidden_refund";
    public static final String REFUND_INITIAL   = "initial";
    public static final String REFUND_PAYBACK   = "payback";
    public static final String REFUND_REFUSED   = "refused";
    public float     base_price;
    public CartGoods goods;
    public int       goods_id;
    public int       quantity;
    public String    refund_state;
    public int       rfl_id;
    public String    shipment_corp;
    public String    shipment_corp_text;
    public String    shipment_no;
    public String    state;
    public String    state_text;
    public float     sum_price;
    public String    url;

    public String refundText() {
        String str = this.refund_state;
        Object obj = -1;
        switch (str.hashCode()) {
            case -2146525273:
                if (str.equals("accepted")) {
                    obj = 2;
                    break;
                }
                break;
            case -787013233:
                if (str.equals("payback")) {
                    obj = 3;
                    break;
                }
                break;
            case -673660814:
                if (str.equals("finished")) {
                    obj = 4;
                    break;
                }
                break;
            case -264108731:
                if (str.equals("can_be_refund")) {
                    obj = null;
                    break;
                }
                break;
            case -123173735:
                if (str.equals("canceled")) {
                    obj = 6;
                    break;
                }
                break;
            case 42567294:
                if (str.equals("forbidden_refund")) {
                    obj = 7;
                    break;
                }
                break;
            case 1085547216:
                if (str.equals("refused")) {
                    obj = 5;
                    break;
                }
                break;
            case 1948342084:
                if (str.equals("initial")) {
                    obj = 1;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                return "申请退款";
            case 1:
                return "退款申请中";
            case 2:
                return "审核通过";
            case 3:
                return "退货中";
            case 4:
                return "退货完成";
            case 5:
                return "退货被拒绝";
            case 6:
                return "退货取消";
            case 7:
                return "";
            default:
                return "";
        }
    }
}
