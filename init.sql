
CREATE TABLE IF NOT EXISTS code_submissions (
        id BIGSERIAL PRIMARY KEY,
    source_code TEXT NOT NULL,
    output TEXT,
    exit_code INTEGER NOT NULL DEFAULT -1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    language VARCHAR(50) NOT NULL,
    execution_time_ms BIGINT DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_code_submissions_language ON code_submissions(language);
CREATE INDEX IF NOT EXISTS idx_code_submissions_created_at ON code_submissions(created_at);
CREATE INDEX IF NOT EXISTS idx_code_submissions_exit_code ON code_submissions(exit_code);
