package com.example.ridepalapplication;

import com.example.ridepalapplication.models.User;

public class MockHelpers {

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("mock_username");
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        mockUser.setEmail("mock_user@user.com");
        mockUser.setPassword("MockPassword98!@");
        return mockUser;
    }

    public static User createAdminMockUser() {
        var mockUser = new User();
        mockUser.setId(2);
        mockUser.setUsername("admin_mock_username");
        mockUser.setFirstName("AdminMockFirstName");
        mockUser.setLastName("AdminMockLastName");
        mockUser.setEmail("admin_mock_user@user.com");
        mockUser.setPassword("Admin_MockPassword98!@");
        mockUser.setAdmin(true);
        return mockUser;
    }

}
