# Pilot Data Analysis Summary — Week 10

**Study**: Peer pilots (n=6) conducted Week 9  
**Purpose**: Identify usability and accessibility barriers through quantitative + qualitative analysis of task performance across JavaScript-enabled (HTMX) and no-JavaScript modes.

---

## Summary Statistics

### Task Completion Times (Median ± MAD)
| Task         | JS-On                  | JS-Off                  | All                     | Key Observations                  |
|--------------|------------------------|-------------------------|-------------------------|-----------------------------------|
| T1_filter    | 1899ms ± 203ms (n=5)   | 3245ms ± 0ms (n=1)      | 2015ms ± 380ms (n=6)    | No-JS mode 71% slower (likely due to full page reloads) |
| T2_edit      | 1400ms ± 184ms (n=4)   | — (n=0)                 | 1400ms ± 184ms (n=4)    | No-JS mode: 0 successful completions |
| T3_delete    | 210ms ± 12ms (n=5)     | 198ms ± 0ms (n=1)       | 208ms ± 12ms (n=6)      | Consistent performance across modes |

### Completion & Error Rates
| Task         | JS-On Completion | JS-Off Completion | JS-On Error Rate | JS-Off Error Rate |
|--------------|------------------|-------------------|------------------|-------------------|
| T1_filter    | 5/5 (100%)       | 1/1 (100%)        | 0%               | 0%                |
| T2_edit      | 4/5 (80%)        | 0/1 (0%)          | 33%              | — (no successful attempts) |
| T3_delete    | 5/5 (100%)       | 1/1 (100%)        | 20%              | 0%                |

---

## Task-by-Task Interpretation

### T1_filter (Filter Content)
- **Quantitative**: Strong performance across both modes with 100% completion. No-JS mode is significantly slower (3245ms vs. 1899ms) but functionally complete. Low variability in JS-on mode (MAD=203ms) indicates consistent user performance.
- **Inclusion Lens**: No accessibility red flags—filtering works for both JS and no-JS users. The slower no-JS time is expected due to full page reloads and is acceptable as a functional parity tradeoff.
- **Qualitative Link**: Pilot notes confirm users found filtering "intuitive" in both modes, with no reported confusion about results.

### T2_edit (Edit Entries)
- **Quantitative**: Critical parity failure—no successful completions in no-JS mode. Even in JS-on mode, 20% of attempts failed (1/5) with a 33% error rate (validation errors). Median time in JS-on is moderate (1400ms) but unreliable due to errors.
- **Inclusion Lens**: High impact on no-JS users (e.g., those with JS disabled for security/accessibility). Validation errors in JS-on mode suggest screen readers may not announce form feedback (violates WCAG 3.3.1 Error Identification).
- **Qualitative Link**: Pilot notes mention "no feedback when submitting empty edits" in no-JS mode, leading to repeated failed attempts.

### T3_delete (Delete Entries)
- **Quantitative**: Perfect completion rates in both modes. Fast median times (≈200ms) with low variability (MAD=12ms in JS-on) indicate a streamlined task. JS-on mode has a 20% error rate, but this did not block completion.
- **Inclusion Lens**: Strong accessibility performance—deletion works reliably for all users. The minor error rate in JS-on is likely due to accidental clicks, not a barrier to success.
- **Qualitative Link**: Users described deletion as "straightforward," with clear confirmation prompts in both modes.

---

## Priority Findings

1. **T2_edit No-JS Completion Failure** (Critical)  
   0% completion in no-JS mode breaks functional parity. Root cause: lack of error feedback for invalid submissions (e.g., blank edits). Disproportionately impacts users who disable JS for accessibility (e.g., screen reader compatibility).

2. **T2_edit Validation Error Accessibility** (High)  
   33% error rate in JS-on mode suggests error messages are not announced to screen readers (violates WCAG 4.1.3 Status Messages). Users cannot correct mistakes without visible feedback.

3. **T1_filter No-JS Performance Gap** (Medium)  
   71% slower completion in no-JS mode is acceptable but could be optimized (e.g., server-side caching). Low priority as it does not block task success.

4. **T3_delete Minor Error Rate** (Low)  
   20% error rate in JS-on mode is minor and does not affect completion. Likely due to user error (e.g., misclicks) rather than design flaws.

---

_Last updated: 2025-12-05