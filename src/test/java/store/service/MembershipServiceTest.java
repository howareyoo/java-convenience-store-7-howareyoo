package store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MembershipServiceTest {

    @Test
    void applayMembershipDiscount() {

        MembershipService membershipService = new MembershipService();
        int notApplicablePrice = 30000;
        boolean isApplicable = true;
        int discount = membershipService.applayMembershipDiscount(isApplicable, notApplicablePrice);
        assertEquals(8000, discount);

    }
}