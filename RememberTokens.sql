USE JavaWebMVC;
GO

-- Store persistent login tokens
CREATE TABLE RememberTokens (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    token_hash CHAR(64) NOT NULL UNIQUE,
    expires_at DATETIME2 NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),

    CONSTRAINT FK_RememberTokens_Users
        FOREIGN KEY (user_id) REFERENCES Users(id)
);
GO

-- Verify the table
SELECT * FROM RememberTokens;
