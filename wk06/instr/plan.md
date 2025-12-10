# Instrumentation Plan (Detailed)

## Logged Events  
- Task creation: `task_add` (timestamp, success/error, JS mode)  
- Task deletion: `task_delete` (timestamp, task ID, success/error, JS mode)  
- Form submission attempts: `form_submit` (timestamp, action type, validation status)  
- Keyboard navigation: `keyboard_event` (timestamp, key pressed, target element)  

## Log Format (CSV Schema)  
`timestamp_iso,session_id,event_type,task_id,status,js_mode,millis`  
- `timestamp_iso`: ISO 8601 format (e.g., 2025-11-08T14:31:22Z)  
- `session_id`: Random 8-character string (no PII)  
- `event_type`: One of `task_add`, `task_delete`, `form_submit`, `keyboard_event`  
- `task_id`: Numeric ID of the task (or `null` for non-task events)  
- `status`: `success`, `validation_error`, or `not_found`  
- `js_mode`: `js_on` or `js_off` (to track progressive enhancement)  
- `millis`: Duration in milliseconds (for actions with completion time)  

## Storage & Privacy  
- Stored locally in `data/metrics.csv` (excluded from public repo via `.gitignore`)  
- No PII: Session IDs are random and not linked to participants  
- Retention: Deleted after Semester 1 (January 2026), per consent protocol  

## Week 9 Integration  
Will be used to measure:  
- Task completion time (keyboard vs. mouse)  
- Error rates for form submissions  
- Success rate of core actions (add/delete) across JS modes  