# Data Recording Notes — Week 9 (2025)

## Data Collection Period
- **Start Date**: November 24, 2025  
- **End Date**: December 4, 2025  
- **Total Duration**: 11 days  

---

## Manual Recording Protocol (Required)

For each participant (P1 to P5), record the following in a physical notebook during sessions, then transcribe to digital format within 24 hours:

### Core Data Points per Task
1. **Task Timing**  
   - Start time (exact timestamp: e.g., “2025-11-25T14:30:15”)  
   - End time (exact timestamp)  
   - Duration (calculated in milliseconds: end - start)  

2. **Completion Status**  
   - Binary indicator: `1` (completed successfully within 180s) or `0` (failed/abandoned/timed out)  

3. **Error Tracking**  
   - Tally of validation errors (e.g., blank submissions, invalid input)  
   - Tally of server errors (e.g., 404/500 responses, failed requests)  
   - Description of each error (e.g., “P2 received ‘title required’ validation on T2_edit at 14:32:08”)  

4. **Confidence Rating**  
   - Post-task score (1–5) as reported by participant  
   - Record exact response (e.g., “P3: ‘3, because I wasn’t sure if the delete confirmed’”)  

5. **Think-Aloud Observations**  
   - Direct quotes in quotation marks (e.g., “P1: ‘Where’s the filter button? I don’t see it’”)  
   - Non-verbal cues (e.g., “hesitated for 8s before clicking ‘delete’”)  

---

## Observational Codes
Use these shorthand codes in notes to streamline recording:  
- `KBD`: Keyboard navigation issue (e.g., “P2 [KBD] couldn’t tab to filter input”)  
- `SR`: Screen reader compatibility problem (e.g., “P5 [SR] no announcement of T3 deletion”)  
- `FOC`: Focus management failure (e.g., “T2_edit [FOC] focus lost after save”)  
- `ERR`: Validation error displayed (e.g., “T1_filter [ERR] no results shown”)  
- `CONF`: Participant expressed confusion (e.g., “P1 [CONF] about ‘meet’ vs ‘meeting’ filter”)  
- `POS`: Positive feedback (e.g., “P4 [POS] praised ‘submit report’ edit flow”)  

---

## Anomaly Documentation
Record all unexpected events that may affect data validity:  
- **Technical issues**: Browser crashes, network timeouts, server errors (include timestamp and resolution)  
- **Participant actions**: Clarifying questions, requests for hints, early termination  
- **Observer errors**: Missed timestamps, incomplete notes (flag for later analysis)  

Example:  
“2025-11-30T15:20: P5’s browser froze during T3_delete (JS off mode). Restarted session, resumed with T3_delete repeat. Duration excluded frozen 45s.”  

---

## Data Storage & Backup
- **Digital files**: Store transcribed notes in `wk09/lab-wk9/data/`  
- **Backup**: Take photos of physical notebook pages after each session, save to `wk09/lab-wk9/evidence/notes-backup/` with filenames like `2025-11-24_P1_notes.jpg`  
- **Anonymization**: Use only pseudonyms (P1-P5) and session IDs (e.g., `s8f2d9`); exclude names, student IDs, or other PII  