# Pilot Evaluation Protocol

## Session Overview
- **Duration**: 15-20 minutes per pilot.  
- **Participants**: 4 peers (mix of JS-on/JS-off users, 1+ screen reader user).  
- **Goal**: Evaluate usability and accessibility of core task flows.  


## Session Flow

### 1. Introduction (2 min)
"Thank you for participating! Today, you’ll test a task manager app by completing 4 simple tasks. We’re evaluating the app, not you—there are no right/wrong answers. Your feedback will help improve the design."


### 2. Consent Confirmation (1 min)
- Confirm understanding of:  
  - No PII collected (you’ll be called "P1/P2/etc.").  
  - Right to stop at any time.  
  - Data stored locally until semester end, then deleted.  
- Record verbal consent in notes (e.g., "P1 confirmed consent at 14:30").  


### 3. Pre-Task Setup (1 min)
- For JS-off pilots: Disable JavaScript in browser settings.  
- For screen reader users: Ensure NVDA/VoiceOver is active.  
- Navigate to `http://localhost:8080/tasks` and confirm page loads.  


### 4. Task Execution (10 min)
- Read each task scenario aloud (e.g., "Task 1: Filter tasks by 'project deadline'").  
- Prompt: "Please think aloud as you complete this task."  
- Do not assist unless:  
  - User is stuck for >2 minutes.  
  - User explicitly asks for help.  
- Record start/end times for each task.  


### 5. Debrief (2 min)
- "How confident were you in completing each task?" (1-5 rating).  
- "Did any part feel confusing or frustrating? Why?"  
- "For screen reader users: Were updates announced clearly?"  


## Moderator Guidelines
- Use neutral prompts: "What are you thinking right now?" (avoid leading questions).  
- Log errors immediately (e.g., "P2 clicked 'Edit' instead of 'Delete'").  
- Note JS mode (on/off) and assistive tools used.  


## Ethics Compliance
- [ ] No real names/IDs recorded (use P1-P4).  
- [ ] Data stored in `data/metrics.csv` (local only, no cloud sync).  
- [ ] Pilots informed of right to withdraw.  
- [ ] Post-session: Offer to review/delete their data.  


**Reference**: UK GDPR (2018), BPS Code of Ethics (2021).