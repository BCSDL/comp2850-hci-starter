Data Boundaries — Week 7
What We Collect (Allowed)
Pseudonymised session IDs: Random 6-character hexadecimal codes prefixed with participant identifiers (e.g., P1_a3f7b9, P2_c8d1e2)
Task metadata: Task titles (limited to module-provided examples like "Buy milk" to avoid sensitive content), completion status (incomplete/complete), start/end timestamps (ISO 8601 format)
Interaction logs: HTMX request types (GET/POST), response status codes (200/400), processing times (in milliseconds)
Accessibility testing notes: Observations of screen reader behavior (e.g., "NVDA announced 'task updated' after save"), keyboard navigation paths (e.g., "Tab moves from edit button to input field"), and focus management outcomes
What We DO NOT Collect (Prohibited)
❌ Real names, aliases, or any personal identifiers (participants referred to as P1, P2, etc.)
❌ Student ID numbers, university emails, or contact information
❌ IP addresses, device identifiers, or browser fingerprints (Ktor server logs configured to exclude this data)
❌ Content of user-generated tasks beyond module examples (no personal to-do items, messages, or sensitive details)
❌ Biometric data, geolocation, or behavioral patterns unrelated to the task interface
Storage & Retention
Location: Local comma-separated values (CSV) files stored in the project's /data directory (data/tasks.csv, data/metrics.csv)
Access: Restricted to the researcher, lab partner (for peer review), and module teaching staff (only upon request for assessment purposes)
Encryption: Protected by standard filesystem permissions (chmod 600) to restrict access to the local user
Retention period: Data retained until the end of Semester 1 (January 2025); after which it will either be:
Anonymised (session IDs replaced with generic identifiers like User_X) for inclusion in portfolio submissions, or
Permanently deleted from local storage and version control history
Privacy by Design Principles Applied
Data minimisation: Only collect session IDs (no names) and task metadata necessary for evaluating interface usability
Purpose limitation: All data used exclusively for HCI module assessments (not shared with third parties or repurposed for other studies)
Storage limitation: Automated deletion timeline aligned with module assessment deadlines (no indefinite retention)
Integrity & confidentiality: Data stored locally (not in cloud services) with private Git repository settings to prevent accidental public exposure
Transparency: Data collection practices documented in consent-protocol.md and shared with participants before data collection
Ethics Risks Identified
Risk	Mitigation
Task titles might inadvertently contain sensitive information (e.g., personal details)	Provide pre-approved example tasks to participants; exclude custom task creation in the interface
Session IDs could be linked to individuals in small peer groups	Use fully randomised IDs (no sequential numbering) and avoid storing any mapping between IDs and participants
Local data files might be accidentally committed to Git	Update .gitignore to explicitly exclude /data/*.csv and verify with git status before commits
Screenshots included in evidence might capture PII (e.g., background desktop content)	Crop screenshots to show only the interface; blur any unintended personal information before saving
Participants may not understand their right to withdraw	Reinforce opt-out process in verbal consent (before sessions) and document withdrawal requests immediately
Reference: ICO (2024). Guide to GDPR, https://ico.org.uk/for-organisations/uk-gdpr-guidance-and-resources/