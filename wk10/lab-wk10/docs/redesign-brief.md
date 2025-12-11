# Inclusive Redesign Brief — Week 10 Lab 2

**Target**: No successful completions in T2_edit (no-JS mode) & Validation errors unannounced to screen readers  
**Priority**: 1 (Score: 8)  
**Assignee**: [Your Name/Pair]  
**Date**: 2025-10-20  


## Problem Statement  
T2_edit (edit entries) exhibits critical accessibility and functional failures across both JavaScript modes. In no-JS mode, pilot data shows 0% completion rate (0/1 attempts) due to missing error feedback for invalid submissions (e.g., blank edits), leaving users unable to correct mistakes (analysis.csv). In JS-on mode, 33% of attempts result in validation errors (2/6 total), with qualitative notes indicating screen reader users cannot identify errors ("no feedback when submitting empty edits" — pilot-notes/P2.md L15-18). These issues violate WCAG 3.3.1 (Error Identification) and 4.1.3 (Status Messages), disproportionately impacting users who disable JS for security/accessibility and screen reader依赖者.  


## Goal  
- Achieve ≥90% completion rate for T2_edit in both JS-on and no-JS modes.  
- Reduce validation error rate to <10% across all modes.  
- Ensure 100% of error messages are programmatically announced to screen readers (verified with NVDA/VoiceOver).  


## Inclusion Impact  
- **No-JS users**: Will gain functional parity by receiving clear error feedback, enabling task completion without JavaScript.  
- **Screen reader users**: Will access real-time error announcements, eliminating "silent failures" that block task success.  
- **Keyboard-only users**: Will benefit from focused error summaries, reducing navigation effort to identify issues.  


## Proposed Changes  

### 1. For No-JS Mode  
- Update server-rendered edit form templates (`templates/tasks/_edit_nojs.peb`) to include an error summary section at the top of the form.  
- Add `tabindex="-1"` and `autofocus` to the error summary when validation fails, ensuring keyboard focus moves to critical feedback.  
- Include clear, actionable error messages (e.g., "Name field cannot be blank — please enter a value").  

### 2. For JS-on Mode  
- Modify HTMX error container in `templates/tasks/_edit_htmx.peb` to include `role="alert"` and `aria-live="assertive"` to trigger automatic screen reader announcements.  
- Link error messages to corresponding form fields using `aria-describedby` for contextual clarity.  


## Acceptance Criteria  
- [ ] T2_edit completion rate ≥90% in post-redesign pilots (n=3) for both JS modes.  
- [ ] Validation error rate ≤10% across all modes.  
- [ ] Error summaries are read aloud by NVDA/VoiceOver within 2 seconds of submission in JS-on mode.  
- [ ] Error summaries receive focus automatically in no-JS mode (verified via keyboard navigation test).  
- [ ] axe DevTools scan shows 0 violations for WCAG 3.3.1 and 4.1.3.  


## Verification Plan  
1. **Automated Testing**: Run axe DevTools on both JS modes to confirm WCAG compliance.  
2. **Manual Testing**:  
   - Keyboard-only navigation: Verify error summary focus in no-JS mode.  
   - Screen reader testing: Use NVDA (Windows) and VoiceOver (macOS) to confirm error announcements.  
3. **Pilot Re-test**: Conduct 3 new pilots (including 1 screen reader user, 1 no-JS user) and compare metrics to pre-redesign baseline (analysis.csv).  


## Risk & Constraints  
- **No-JS auto-focus limitation**: `autofocus` may not work reliably in older browsers; mitigate by adding a "Jump to errors" link as fallback.  
- **HTMX-ARIA conflict**: `aria-live` regions may interfere with HTMX swap behavior; test with `hx-swap="outerHTML"` to preserve live region properties.  
- **Time constraint**: Implementing both fixes may require 45 minutes; prioritize no-JS completion if time is limited.  


_Last updated: 2025-12-05