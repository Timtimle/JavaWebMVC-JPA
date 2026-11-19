USE JavaWebMVC;
GO

IF OBJECT_ID('dbo.PendingEmailChanges', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.PendingEmailChanges (
        user_id INT NOT NULL PRIMARY KEY,
        new_email NVARCHAR(255) NOT NULL,
        code_hash VARCHAR(64) NOT NULL,
        created_at DATETIME2 NOT NULL,
        expires_at DATETIME2 NOT NULL,
        attempts INT NOT NULL DEFAULT 0,
        CONSTRAINT FK_PendingEmailChanges_Users FOREIGN KEY (user_id)
            REFERENCES dbo.Users(id) ON DELETE CASCADE
    );
END;
GO

IF COL_LENGTH('dbo.Users', 'avatar') IS NOT NULL
BEGIN
    ALTER TABLE dbo.Users ALTER COLUMN avatar NVARCHAR(1000) NULL;
END;
GO
