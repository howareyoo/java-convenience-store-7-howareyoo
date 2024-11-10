package store.service;

import store.contents.MembershipDiscount;

public class MembershipService {

    public int applayMembershipDiscount(boolean isApply, int notApplicablePrice) {
        if (isApply) {
            float discountRate = MembershipDiscount.MEMBERSHIP_DISCOUNT.getDiscountRate();
            int maxDiscount = MembershipDiscount.MEMBERSHIP_DISCOUNT.getMaxDiscount();

            int discount = (int) (notApplicablePrice * discountRate);

            if (discount > maxDiscount) {
                discount = maxDiscount;
            }
            return discount;
        }

        return 0;
    }
}
