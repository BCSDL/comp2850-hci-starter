# Data Boundaries — Week 7

## What We Collect (Allowed)
- **Pseudonymised session identifiers**: Random 6-character alphanumeric codes (e.g., `S7b9x2`) to track individual test sessions without linking to personal identity.
- **Task metadata**: Task titles (limited to example content like "Submit assignment" or "Review notes"), completion status (pending/complete), and timestamps of creation/editing/deletion.
- **Interaction logs**: Types of user actions (add/edit/delete), response times for HTMX requests, and error codes (e.g., 400 for invalid input) to evaluate system performance.
- **Accessibility testing observations**: Notes on assistive technology behavior (e.g., "NVDA announced 'task updated' correctly") and keyboard navigation patterns (e.g., "Tab order maintained in edit mode").
- **Screenshots**: Cropped images of the interface focusing only on task management elements (no personal or contextual information visible).

## What We DO NOT Collect (Prohibited)
- ❌ Personal identifiers: Names, student IDs, email addresses, or usernames.
- ❌ Contact information: Phone numbers, social media handles, or physical addresses.
- ❌ Device or network data: IP addresses, device identifiers, browser history, or geolocation.
- ❌ Sensitive content: Task titles containing personal, academic, or confidential information (participants are instructed to use provided examples).
- ❌ Biometric or behavioral data beyond interaction logs: No keystroke dynamics, facial recognition, or voice recordings.

## Storage & Retention Policies
- **Storage location**: All data is stored locally on the researcher’s laptop in encrypted CSV files (`data/tasks.csv`, `data/metrics.csv`) and plain-text notes (`wk07/ethics/observations.md`).
- **Access restrictions**: Access is limited to the primary researcher, their lab partner, and module teaching staff (only for assessment purposes). File permissions are set to `chmod 600` (read/write for owner only).
- **Retention period**: Data is retained until the end of Semester 1 (January 2025). After this date, raw data is either:
  - Permanently deleted from local storage, or
  - Anonymised (session IDs removed, task metadata generalized) for inclusion in academic portfolios.
- **Transfer limitations**: No data is transferred to cloud services, external servers, or third parties without full anonymization.

## Privacy by Design Principles Applied
1. **Data minimisation**: Only collect data necessary to evaluate task management functionality and accessibility (e.g., no redundant timestamps or unused interaction logs).
2. **Purpose limitation**: All data is used exclusively for HCI coursework analysis (usability testing, accessibility audits) and not repurposed for other research or commercial activities.
3. **Storage limitation**: Automated scripts delete raw data after the retention period, with manual verification to ensure no residual files remain.
4. **Integrity & confidentiality**: Local storage (no cloud sync) and private Git repositories (with `.gitignore` excluding sensitive data) prevent unauthorized access.
5. **Transparency**: Participants are informed of data usage via the Week 6 consent protocol, with clear opt-out mechanisms.

## Ethics Risks Identified with Mitigations
| Risk Scenario | Mitigation Strategy |
|---------------|---------------------|
| Task titles may accidentally contain sensitive information (e.g., personal reminders). | Provide pre-approved example tasks (e.g., "Buy groceries") and explicitly instruct participants to avoid personal content. Review all task data post-session and redact sensitive entries. |
| Session IDs could be cross-referenced with other data to identify participants in small cohorts. | Use fully random IDs with no sequential numbering; never store mapping between IDs and participants. Anonymize IDs in analysis by replacing with generic labels (e.g., "User A"). |
| Git repository containing data files may be made public accidentally. | Configure `.gitignore` to exclude all data files (`/data/`, `*.csv`), verify repository privacy settings weekly, and use local backups instead of remote storage for raw data. |
| Screenshots may capture unintended PII (e.g., browser tabs with emails). | Train researchers to crop screenshots to the task management interface only. Use a script to automatically blur non-UI regions before saving. |
| Participants may withdraw consent post-session but data is already stored. | Maintain a log of opt-out requests, with a 48-hour deletion process. Clearly document this process in the consent protocol and provide contact details for withdrawal. |

**Reference**: ICO (2024). *Guide to Data Protection by Design and Default*. <https://ico.org.uk/for-organisations/uk-gdpr-guidance-and-resources/accountability-and-governance/guide-to-accountability-and-governance/accountability-and-governance/data-protection-by-design-and-default/>
```