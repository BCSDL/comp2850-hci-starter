```markdown
# Task 1: Findings & Evidence Chains

**Purpose**: Synthesize pilot data into actionable findings with evidence chains linking raw data to backlog items.

---

## Executive Summary

**Pilots conducted**: 3 participants
**Date range**: 2025-10-15 to 2025-10-15
**Variants tested**: Standard (HTMX, JS-On), no-JS (traditional, JS-Off)

**Key findings**:
1. T3_add (Add) has critical no-JS parity failure with 50% completion rate and 509.5% slower median time (WCAG 2.1.1 Keyboard)
2. T2_edit (Edit) has 50% error rate across both JS modes due to unclear validation feedback, disproportionately impacting users with cognitive disabilities
3. T1_filter (Filter) shows minor performance differences between JS modes but maintains functional parity

**Recommended fixes for Week 10**: 
- Implement client-side validation for T3_add in no-JS mode to reduce errors and load times
- Add ARIA live regions and explicit error messaging for T2_edit to improve validation feedback
- Optimize T3_add no-JS submission flow to minimize page reload delays

---

## 1. Quantitative Results

### 1.1 Completion Rates

| Task         | Code  | Completion       | Notes                                  |
|--------------|-------|------------------|----------------------------------------|
| Filter       | T1    | 2 / 2 (100%)     | No failures in either JS mode          |
| Edit         | T2    | 2 / 3 (66.7%)    | 1 failure in JS-On (P3_5e7g, step=fail) |
| Add          | T3    | 2 / 3 (66.7%)    | 1 validation error in JS-Off (P2_3c8d)  |
| Delete       | T4    | 2 / 2 (100%)     | No failures in either JS mode          |

**Analysis**: T2_edit and T3_add have the lowest completion rates. T2_edit failure was due to a system error (step=fail) in JS-On mode, while T3_add failure was a validation error (max_length) in JS-Off mode. T1 and T4 showed perfect completion, indicating stable core functionality.

**Evidence**:
- `04-results.csv` lines 10: P3_5e7g,req_010,T2_edit,fail,,0,500,on (T2 failure)
- `04-results.csv` lines 7: P2_3c8d,req_007,T3_add,validation_error,max_length,0,400,off (T3 validation error)
- `06-evidence/pilot-notes/P3-notes.md` line 15: "P3: 'The edit function just stopped working—no feedback'"

---

### 1.2 Task Completion Times

**Median times** (success only):

| Task         | Code  | Median (ms) | Median (s) | MAD (ms) | Range       | n |
|--------------|-------|-------------|------------|----------|-------------|---|
| Filter       | T1    | 1973.5      | 2.0s       | 126.5    | 1.8s–2.1s   | 2 |
| Edit         | T2    | 1400        | 1.4s       | 160      | 1.2s–1.6s   | 2 |
| Add          | T3    | 2011.5      | 2.0s       | 2889     | 0.6s–3.5s   | 2 |
| Delete       | T4    | 204         | 0.2s       | 12       | 0.2s–0.2s   | 2 |

**Analysis**: T3_add shows the highest variability (MAD=2889ms) due to extreme difference between JS modes (567ms vs 3456ms). T4_delete is consistently fast across all sessions. T2_edit has moderate variability, likely due to user familiarity with the task.

**Evidence**:
- `04-results.csv`: Filtered `step=success` for all tasks (lines 1,3,4,5,6,8,9,11,12)
- `06-evidence/pilot-notes/P2-notes.md` line 8: "P2: 'Adding the task took forever without JS—had to wait for the page to reload'"

---

### 1.3 Error Rates

| Task         | Code  | Validation Errors | Total Attempts | Error Rate | Notes                  |
|--------------|-------|-------------------|----------------|------------|------------------------|
| Filter       | T1    | 0                 | 2              | 0%         | No errors recorded     |
| Edit         | T2    | 1                 | 2              | 50%        | blank_title (JS-On)    |
| Add          | T3    | 1                 | 2              | 50%        | max_length (JS-Off)    |
| Delete       | T4    | 0                 | 2              | 0%         | No errors recorded     |

**Analysis**: T2_edit and T3_add have identical 50% error rates but with different error types. T2_edit's blank_title error suggests users may not recognize required fields, while T3_add's max_length error indicates no pre-submission validation in JS-Off mode. No errors in T1 and T4 indicate clear user flows.

**Evidence**:
- `04-results.csv`: Filtered `step=validation_error` (lines 2,7)
- `06-evidence/pilot-notes/P1-notes.md` line 6: "P1: 'I didn't realize the title was required—no warning before submitting'"

---

### 1.4 JS-On vs JS-Off Comparison

**Task T3_add comparison** (most impacted by JS):

| Variant       | Median Time (ms) | n | Notes                          |
|---------------|------------------|---|--------------------------------|
| JS-on (HTMX)  | 567              | 1 | Smooth AJAX updates            |
| JS-off        | 3456             | 1 | Full page reloads post-submit  |
| **Difference**| 6.1× slower      |   | Exceeds acceptable parity threshold |

**Analysis**: T3_add in JS-Off mode is over 6x slower than JS-On, primarily due to full page reloads. The validation error in JS-Off (max_length) suggests missing client-side checks that exist in JS-On, breaking parity. This creates a significant barrier for users who disable JS.

**Evidence**:
- `04-results.csv`: Filtered `js_mode=off` for session P2_3c8d (lines 6-9)
- `06-evidence/pilot-notes/P2-notes.md` line 10: "P2: 'Got an error about length after submitting, but it didn't tell me before I clicked'"

---

## 2. Qualitative Findings

### 2.1 Accessibility Issues

**Finding A1**: T3_add in JS-Off mode lacks accessible validation and performance, violating WCAG parity requirements

**Severity**: High (WCAG 2.1.1 Keyboard)

**Affected users**: No-JS users (including those with security constraints, older browsers, or low bandwidth)

**Evidence chain**:
1. **Raw data**: `04-results.csv` line 7: P2_3c8d,req_007,T3_add,validation_error,max_length,0,400,off
2. **Observation**: `06-evidence/pilot-notes/P2-notes.md` line 9: "P2: 'Took 3 seconds to get the error message—had to wait for the page to reload'"
3. **Screenshot**: `06-evidence/screenshots/t3-nojs-validation.png` showing error without pre-submission warning
4. **WCAG reference**: 2.1.1 Keyboard (A) — All functionality must be available via keyboard, with comparable performance

**Impact**:
- **Inclusion**: No-JS users face unnecessary delays and errors, excluding them from core functionality
- **Frequency**: 50% of T3_add attempts in JS-Off failed due to validation
- **Criticality**: Blocks task completion for a subset of users, violating accessibility standards

**Proposed mitigation**: Implement server-side pre-validation for JS-Off mode with inline error hints; optimize reloads using partial page updates

**Backlog item**: wk9-01 (see `backlog/backlog.csv`)

---

**Finding A2**: T2_edit validation errors lack proper announcement for screen reader users

**Severity**: High (WCAG 3.3.1 Error Identification)

**Affected users**: Screen reader users, users with cognitive disabilities

**Evidence chain**:
1. **Raw data**: `04-results.csv` line 2: P1_7a9f,req_002,T2_edit,validation_error,blank_title,0,400,on
2. **Observation**: `06-evidence/pilot-notes/P1-notes.md` line 5: "P1: 'I submitted without a title and nothing happened—no message'"
3. **Screenshot**: `06-evidence/screenshots/t2-error-sr.png` showing error div missing `role="alert"`
4. **WCAG reference**: 3.3.1 Error Identification (A) — Errors must be identified and described to users

**Impact**:
- **Inclusion**: SR users cannot detect validation errors, leading to repeated failed attempts
- **Frequency**: 50% of T2_edit attempts across modes had validation errors
- **Criticality**: Prevents task completion for users relying on assistive technology

**Proposed mitigation**: Add `aria-live="assertive"` to error containers; link errors to input fields with `aria-describedby`

**Backlog item**: wk9-02 (see `backlog/backlog.csv`)

---

### 2.2 Usability Issues

**Finding U1**: T1_filter auto-refresh confuses users expecting explicit action

**Severity**: Medium (UX issue, not WCAG violation)

**Evidence chain**:
1. **Observation**: `06-evidence/pilot-notes/P1-notes.md` line 3: "P1: 'I typed my filter and waited for a button to click, but it just changed'"
2. **Observation**: `06-evidence/pilot-notes/P3-notes.md` line 4: "P3: 'Wasn't sure if it was done filtering or still loading'"
3. **Screenshot**: `06-evidence/screenshots/t1-filter-ux.png` showing no loading indicator

**Impact**:
- **Inclusion**: Low (all users eventually complete the task)
- **Frequency**: 2/3 participants hesitated during T1_filter
- **Criticality**: Causes minor delays but no task failures

**Proposed mitigation**: Add "Results update automatically" help text; include subtle loading spinner during filter application

**Backlog item**: wk9-03 (see `backlog/backlog.csv`)

---

### 2.3 Positive Observations

**What worked well** (keep in redesign):

1. T4_delete maintains perfect parity across JS modes with fast completion times
   - Evidence: `06-evidence/pilot-notes/P1-notes.md` line 8: "P1: 'Deleting was straightforward—no confusion at all'"

2. Keyboard navigation tab order is logical in both JS modes
   - Evidence: `06-evidence/pilot-notes/P2-notes.md` line 5: "P2: 'Could tab through all buttons easily, even without JS'"

3. T1_filter functional parity ensures consistent results regardless of JS mode
   - Evidence: `04-results.csv`: Sessions P1_7a9f (JS-On) and P2_3c8d (JS-Off) achieved identical filter outcomes

---

## 3. Prioritization for Week 10

**Prioritization formula**: `(Impact + Inclusion) – Effort` (all 1–5 scale)

| ID      | Issue                                          | Impact | Inclusion | Effort | Score | Priority | Week 10? |
|---------|------------------------------------------------|--------|-----------|--------|-------|----------|----------|
| wk9-01  | T3_add no-JS validation & performance issues   | 5      | 5         | 3      | 7     | High     | ✅       |
| wk9-02  | T2_edit inaccessible error messaging           | 5      | 5         | 2      | 8     | High     | ✅       |
| wk9-03  | T1_filter auto-refresh confusion               | 3      | 2         | 1      | 4     | Medium   | ⏸       |

**Rationale**:
- **Impact**: Severity of problem (5 = blocks task, 1 = minor annoyance)
- **Inclusion**: Affects marginalized users (5 = SR/keyboard only, 1 = all users equally)
- **Effort**: Development + testing time (5 = >4 hours, 1 = <30 min)

**Week 10 focus**: wk9-01, wk9-02 — highest inclusion impact with feasible implementation effort

---

## 4. Evidence Chains (Summary)

**Purpose**: Ensure every claim is traceable to data (no assumptions).

**Format**: `Raw data → Observation → Interpretation → Backlog item → Proposed fix`

### Example: Finding A1 (T3_add no-JS parity failure)

```
04-results.csv line 7: P2_3c8d,req_007,T3_add,validation_error,max_length,0,400,off
    ↓
06-evidence/pilot-notes/P2-notes.md line 9: "P2: 'Took 3 seconds to get the error message—had to wait for the page to reload'"
    ↓
Interpretation: No-JS users face delayed feedback and validation errors due to missing client-side checks (WCAG 2.1.1)
    ↓
backlog/backlog.csv wk9-01: "Implement server-side pre-validation for T3_add (no-JS) + partial reloads"
    ↓
Week 10 fix: Add maxlength attribute to input in no-JS template; implement AJAX-like partial updates via HTMX fallback
```

### Example: Finding A2 (T2_edit inaccessible errors)

```
04-results.csv line 2: P1_7a9f,req_002,T2_edit,validation_error,blank_title,0,400,on
    ↓
06-evidence/pilot-notes/P1-notes.md line 5: "P1: 'I submitted without a title and nothing happened—no message'"
    ↓
Interpretation: Error messages lack programmatic announcement, blocking SR users (WCAG 3.3.1)
    ↓
backlog/backlog.csv wk9-02: "Add ARIA live regions to T2_edit error container"
    ↓
Week 10 fix: Update error div with aria-live="assertive" and aria-describedby linking to input
```

**All findings must follow this structure** for credibility and transparency.

---

## 5. Limitations & Future Work

### 5.1 Study Limitations

1. **Sample size**: 3 participants (small, but sufficient for formative evaluation per Nielsen's 5-user heuristic)
2. **Diversity**: Limited to peer participants with similar technical literacy; missing perspectives from older adults or users with disabilities
3. **Context**: Lab environment (may not reflect real-world usage patterns like distractions or time constraints)
4. **Tasks**: Scripted scenarios (real-world tasks might reveal different failure modes)

### 5.2 Data Quality Issues

- No missing data, but T3_add in JS-Off only has 1 successful attempt (small sample for time calculations)
- No excluded data—all sessions completed the protocol as designed
- Potential facilitator bias: Peer pilots may have prior knowledge of system behavior

### 5.3 Future Evaluations

**For Week 10 re-pilots** (post-redesign):
- Focus on T2_edit and T3_add to verify fixes for errors and performance
- Reuse same tasks and metrics for direct comparability
- Target 2 additional participants (1 JS-On, 1 JS-Off) for regression testing

**For professional practice**:
- Recruit diverse participants (age 18–70+, varying technical skill, including users with disabilities)
- Include real-world tasks (e.g., "Edit a task you created last week")
- Conduct longitudinal testing over 2 weeks to observe sustained usage patterns

---

## 6. Conclusion

**Summary of key findings**:
1. T3_add in JS-Off mode has critical parity issues with slow performance and validation errors, excluding no-JS users
2. T2_edit validation errors lack accessible messaging, blocking screen reader users and those with cognitive disabilities
3. T1_filter works reliably but causes minor confusion due to auto-refresh behavior

**Recommended actions**:
- Week 10 Lab 1: Use `Analyse.kt` to deepen T3_add performance analysis; finalize fix details
- Week 10 Lab 2: Implement ARIA live regions for T2_edit and pre-validation for T3_add (no-JS); re-pilot to verify
- Week 11: Present evidence of improved parity and accessibility in studio critique

**Learning Outcomes addressed**:
- **LO6**: Iterative design (pilot → findings → redesign)
- **LO8**: Evaluation execution (protocol, metrics, analysis)
- **LO11**: Collaboration (peer pilots, observer role)
- **LO12**: Professionalism (consent, evidence chains, honest reporting)

---

## Appendices

### Appendix A: WCAG 2.2 References

- **2.1.1 Keyboard (A)**: All functionality must be available via a keyboard interface without requiring specific timings for individual keystrokes
- **3.3.1 Error Identification (A)**: If an input error is automatically detected, the item that is in error is identified and the error is described to the user in text
- **3.3.3 Error Suggestion (AA)**: If an input error is automatically detected and suggestions for correction are known, then the suggestions are provided to the user
- **4.1.3 Status Messages (AA)**: In content implemented using markup languages, status messages can be programmatically determined by assistive technologies without receiving focus

See [WCAG 2.2 Quick Reference](https://www.w3.org/WAI/WCAG22/quickref/) for full criteria.

### Appendix B: Definitions

- **MAD** (Median Absolute Deviation): Robust measure of variability (less affected by outliers than standard deviation)
- **PRG** (POST-Redirect-GET): Pattern to prevent form resubmission on browser refresh by redirecting after POST
- **SR** (Screen Reader): Assistive technology that reads screen content aloud (e.g., NVDA, JAWS, VoiceOver)
- **HTMX**: Library for enabling AJAX, CSS transitions, and more using HTML attributes (supports progressive enhancement)

See [Glossary](../../references/glossary.md) for full definitions.

---

**Author**: [Yiming Xu]
**Date**: 2025-10-16
**Version**: Draft for Task 1 submission
**Next review**: Week 11 (finalize for portfolio)
```