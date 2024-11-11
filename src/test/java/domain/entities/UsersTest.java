package domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class UsersTest {
    Users user1;
    UUID compId1;
    Instant creationTime1;

    @BeforeEach
    public void setup() {
        user1 = Mockito.mock(Users.class);
        compId1 = UUID.randomUUID();
        creationTime1 = Instant.now();

        when(user1.getName()).thenReturn("User 1");
        when(user1.getEmail()).thenReturn("user1@test.com");
        when(user1.getIsOwner()).thenReturn(true);
        when(user1.getTitle()).thenReturn("CEO");
        when(user1.getCompanyId()).thenReturn(compId1);
        when(user1.getIsActive()).thenReturn(true);
        when(user1.getCreatedAt()).thenReturn(creationTime1);
        when(user1.getUpdatedAt()).thenReturn(creationTime1);
    }

    @Test
    public void testGetter() {
        assert user1.getName().equals("User 1");
        assert user1.getEmail().equals("user1@test.com");
        assert user1.getIsActive().equals(true);
        assert user1.getIsOwner().equals(true);
        assert user1.getTitle().equals("CEO");
        assert user1.getCompanyId().equals(compId1);
        assert user1.getCreatedAt().equals(creationTime1);
        assert user1.getUpdatedAt().equals(creationTime1);
    }
}
