-- Create application_history table for tracking employee application operations
CREATE TABLE application_history (
    id BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    from_status VARCHAR(50),
    to_status VARCHAR(50),
    operator_id BIGINT,
    description TEXT,
    details TEXT,
    workflow_task_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    client_ip VARCHAR(45),
    user_agent TEXT,

    -- Foreign key constraints
    CONSTRAINT fk_application_history_application
        FOREIGN KEY (application_id) REFERENCES employee_applications(id) ON DELETE CASCADE,
    CONSTRAINT fk_application_history_operator
        FOREIGN KEY (operator_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Create indexes for better query performance
CREATE INDEX idx_application_history_application_id ON application_history(application_id);
CREATE INDEX idx_application_history_action_type ON application_history(action_type);
CREATE INDEX idx_application_history_operator_id ON application_history(operator_id);
CREATE INDEX idx_application_history_created_at ON application_history(created_at);
CREATE INDEX idx_application_history_workflow_task_id ON application_history(workflow_task_id);

-- Add comment for documentation
COMMENT ON TABLE application_history IS 'Records all operations performed on employee applications for audit and tracking purposes';
COMMENT ON COLUMN application_history.application_id IS 'Reference to the employee application';
COMMENT ON COLUMN application_history.action_type IS 'Type of action performed (CREATED, SUBMITTED, REVIEWED, etc.)';
COMMENT ON COLUMN application_history.from_status IS 'Application status before the action';
COMMENT ON COLUMN application_history.to_status IS 'Application status after the action';
COMMENT ON COLUMN application_history.operator_id IS 'User who performed the action (NULL for system actions)';
COMMENT ON COLUMN application_history.description IS 'Human-readable description of the action';
COMMENT ON COLUMN application_history.details IS 'Additional details in JSON format';
COMMENT ON COLUMN application_history.workflow_task_id IS 'Associated workflow task ID if applicable';
COMMENT ON COLUMN application_history.client_ip IS 'IP address of the client that initiated the action';
COMMENT ON COLUMN application_history.user_agent IS 'User agent string of the client';