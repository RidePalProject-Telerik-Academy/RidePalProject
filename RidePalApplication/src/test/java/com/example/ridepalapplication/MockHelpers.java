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

}
