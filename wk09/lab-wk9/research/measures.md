# Evaluation Metrics & Measures

## Objective Metrics
| Metric               | Definition                                                                 | Calculation                                                                 |
|----------------------|-----------------------------------------------------------------------------|-----------------------------------------------------------------------------|
| **Task Completion Time** | Time from task start (user begins action) to success criteria met.         | Median (all pilot times) ± MAD (Median Absolute Deviation).                 |
| **Completion Rate**   | Percentage of pilots who completed the task without assistance.             | (Completed tasks / Total attempts) × 100.                                   |
| **Error Rate**        | Number of errors per task (e.g., failed validation, wrong button clicks).   | (Total errors across pilots / Total attempts) × 100.                        |
| **Recovery Rate**     | Percentage of errors that users self-corrected without help.                | (Corrected errors / Total errors) × 100.                                    |


## Subjective Metrics
| Metric               | Scale                                                                       | Collection Method                                                           |
|----------------------|-----------------------------------------------------------------------------|-----------------------------------------------------------------------------|
| **Confidence**        | 1-5 (1 = "Not confident at all", 5 = "Extremely confident").                | Verbal rating after each task.                                              |
| **Perceived Ease**    | UMUX-Lite item: "This task was easy to complete." (1-7, 7 = "Strongly agree"). | Post-session questionnaire.                                                 |
| **Accessibility Rating** | 1-5 (1 = "Very difficult for screen readers/keyboard", 5 = "Very accessible"). | Verbal feedback from pilots using assistive tools.                          |


## Data Sources
- **Server logs**: `data/metrics.csv` (task completion time, errors, JS mode).  
- **Observer notes**: `wk09/research/pilot-notes.md` (qualitative errors, recovery actions).  
- **Pilot feedback**: Recorded after each task (confidence, ease).  


## Thresholds for Concern
- Completion rate < 70%: Task design needs revision.  
- Error rate > 30%: UI element (e.g., button, form) is misleading.  
- Median time > 2× target (e.g., >60s for Task 1): Efficiency issue.