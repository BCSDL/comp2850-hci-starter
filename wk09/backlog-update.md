# Backlog Updates

## New Items Added

| ID | Description | Priority | Effort | Notes |
|--|--|--|--|--|
| #13 | Focus indicator contrast below WCAG AAA (2.8:1) | P2 | 2 | Feedback from Week 11 studio crit (Team Beta); WCAG 2.2 Focus Appearance. Fix: Increase focus outline to darker blue (#0000aa) for 3:1 contrast |
| #14 | Verify SR compatibility with VoiceOver (macOS) | P1 | 3 | Identified in Week 11 crit (Team Alpha). Evidence: `wk11/crit/feedback-received.md`. Action: Test with VoiceOver, document findings |
| #15 | Remaining 25% error rate on T2—investigate cause | P1 | 4 | From staff feedback in Week 11 crit. Evidence: `submission-template.md Section 5 Part B; wk11/crit/feedback-received.md`. Plan: Run additional pilot focusing on error triggers, check focus management |
| #16 | aria-live=assertive considered intrusive by users | P2 | 3 | Feedback from Team Beta (Week 11 crit). Need to test polite vs assertive live regions. Evidence: `wk11/crit/feedback-received.md` |
| #17 | Add maxlength counter for task input | P3 | 2 | Suggestion from Team Delta (Week 11 crit). Progressive enhancement, nice-to-have for usability |

## Priority Rationale

Priority scores calculated using formula: `(Impact + Inclusion) - Effort`  
- #14: (5 Impact + 5 Inclusion) - 3 Effort = 7 → P1  
- #15: (5 Impact + 4 Inclusion) - 4 Effort = 5 → P1  
- #13: (4 Impact + 3 Inclusion) - 2 Effort = 5 → P2  
- #16: (3 Impact + 3 Inclusion) - 3 Effort = 3 → P2  
- #17: (2 Impact + 1 Inclusion) - 2 Effort = 1 → P3  