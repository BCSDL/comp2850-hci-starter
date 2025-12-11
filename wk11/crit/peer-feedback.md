# Peer Feedback — Studio Crit

   Date: 2025-12-12

## 1) Clarity
- Problem statement is specific and backed by evidence: ☐Yes ☑Partly ☐No
- Notes: The team identified "low completion rate for inline edit tasks" as the core problem, supported by Week 9 pilot data (67% overall, 0% for no-JS users). However, the link between the problem and WCAG criteria (e.g., 3.3.1 Error Identification) was not explicitly stated, reducing clarity.


## 2) Inclusion impact
- Groups who benefit / potential gaps:
  - Beneficiaries: Screen reader users (via improved `aria-live` announcements) and keyboard-only users (fixed focus management).
  - Potential gaps: Users with cognitive disabilities (no evidence of testing with simplified language needs) and older adults with age-related vision changes (no zoom testing beyond 125%).
- Notes: The team focused on technical accessibility (SR, keyboard) but lacked data on situational or temporary impairments (e.g., using the tool in bright sunlight).


## 3) Accessibility
- Status messages / error linking / keyboard path observations:
  - Status messages: `aria-live=assertive` works in HTMX mode but interrupts screen reader navigation (observed in live demo).
  - Error linking: Error summaries lack `id` attributes to link to form fields (violates WCAG 3.3.1).
  - Keyboard path: Tab order skips the "Clear Filters" button in no-JS mode.
- Notes: High-priority fixes addressed critical barriers, but minor issues (e.g., focus indicator contrast 2.8:1) remain unaddressed.


## 4) Parity (No-JS vs HTMX)
- Observed parity issues, if any:
  - HTMX mode shows real-time character count for task titles; no-JS mode lacks this feature.
  - Delete confirmation dialog appears in HTMX mode but not in no-JS mode (users must rely on browser back button).
- Notes: Core CRUD functionality works in both modes, but progressive enhancement features create inconsistent user experiences.


## 5) Next steps (one concrete suggestion)
- Add a server-rendered delete confirmation page for no-JS mode, with a "Confirm" button that returns focus to the task list after submission. This would align with HTMX behavior and meet WCAG 3.3.4 Error Prevention.