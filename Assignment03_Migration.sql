USE JavaWebMVC;
GO

-- Preserve existing accounts: only newly registered users require activation.
IF COL_LENGTH('dbo.Users', 'is_active') IS NULL
BEGIN
    ALTER TABLE dbo.Users
    ADD is_active BIT NOT NULL CONSTRAINT DF_Users_is_active DEFAULT (1) WITH VALUES;
END;
GO

-- Store expiring, hashed email OTPs for activation and password reset.
IF OBJECT_ID('dbo.EmailOtps', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.EmailOtps (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        purpose VARCHAR(20) NOT NULL,
        code_hash CHAR(64) NOT NULL,
        expires_at DATETIME2 NOT NULL,
        created_at DATETIME2 NOT NULL,
        attempts INT NOT NULL DEFAULT 0,
        CONSTRAINT FK_EmailOtps_Users FOREIGN KEY (user_id) REFERENCES dbo.Users(id),
        CONSTRAINT CK_EmailOtps_purpose CHECK (purpose IN ('ACTIVATE','RESET')),
        CONSTRAINT UQ_EmailOtps_user_purpose UNIQUE (user_id,purpose)
    );
END;
GO

-- One User owns many Videos. Media files are stored outside the deployed WAR.
IF OBJECT_ID('dbo.Videos','U') IS NULL
BEGIN
    CREATE TABLE dbo.Videos (
        video_id BIGINT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        title NVARCHAR(255) NOT NULL,
        description NVARCHAR(MAX) NULL,
        file_name VARCHAR(255) NOT NULL,
        views BIGINT NOT NULL CONSTRAINT DF_Videos_views DEFAULT (0),
        created_at DATETIME2 NOT NULL CONSTRAINT DF_Videos_created DEFAULT (GETDATE()),
        CONSTRAINT FK_Videos_Users FOREIGN KEY (user_id) REFERENCES dbo.Users(id)
    );
    CREATE INDEX IX_Videos_latest ON dbo.Videos(created_at DESC,video_id DESC);
    CREATE INDEX IX_Videos_owner ON dbo.Videos(user_id,created_at DESC);
END;
GO

SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_NAME IN ('Users','Category','RememberTokens','EmailOtps','Videos');
GO
