# Pilot Data Analysis Summary — Week 10

**Study**: Peer pilots (n=3) conducted Week 9  
**Purpose**: Identify usability and accessibility barriers through quantitative + qualitative analysis

---

## Summary Statistics

### Task Completion Times (Median ± MAD)

| Task       | JS-On                  | JS-Off                 | All                     | Notes                  |
|------------|------------------------|------------------------|-------------------------|------------------------|
| T1_filter  | 1847ms ± 0ms (n=1)     | 2100ms ± 0ms (n=1)     | 1973.5ms ± 253ms (n=2)  | JS-Off 13.7% slower    |
| T2_edit    | 1560ms ± 326ms (n=2)   | 1234ms ± 0ms (n=1)     | 1400ms ± 160ms (n=3)    | JS-On 26.4% slower     |
| T3_add     | 567ms ± 0ms (n=1)      | 3456ms ± 0ms (n=1)     | 2011.5ms ± 2889ms (n=2) | JS-Off 509.5% slower   |
| T4_delete  | 210ms ± 0ms (n=1)      | 198ms ± 0ms (n=1)      | 204ms ± 12ms (n=2)      | Consistent performance |

### Completion & Error Rates

| Task       | JS-On Completion | JS-Off Completion | JS-On Errors | JS-Off Errors |
|------------|------------------|-------------------|--------------|---------------|
| T1_filter  | 1/1 (100%)       | 1/1 (100%)        | 0%           | 0%            |
| T2_edit    | 1/2 (50%)        | 1/2 (50%)         | 50%          | 50%           |
| T3_add     | 1/1 (100%)       | 1/2 (50%)         | 0%           | 50%           |
| T4_delete  | 1/1 (100%)       | 1/1 (100%)        | 0%           | 0%            |

---

## Task-by-Task Interpretation

### T1_filter (Filter)
- **Quantitative**: 100% completion rate in both JS modes, with minimal time difference (JS-Off slightly slower). No errors recorded.
- **Inclusion Lens**: Functionally consistent across JS modes, suggesting good parity for users relying on no-JS (e.g., low-bandwidth or assistive tech users).
- **Key Insight**: Stable performance indicates the filter task is well-designed for both environments.

### T2_edit (Edit)
- **Quantitative**: 50% completion rate in both modes, with 50% error rate (validation error: blank title). Median time varies but within acceptable range.
- **Inclusion Lens**: High error rate suggests validation feedback may be unclear, particularly impacting users with cognitive disabilities who need explicit guidance. No-JS users face the same barrier as JS-On users.
- **Key Insight**: Critical need to improve error messaging (e.g., ARIA alerts for screen readers, prominent visual cues).

### T3_add (Add)
- **Quantitative**: 100% completion in JS-On, but 50% in JS-Off (validation error: max length). JS-Off takes over 5x longer.
- **Inclusion Lens**: No-JS users experience significant delays and failures, indicating parity breakdown. Users relying on no-JS (e.g., security-conscious or older browsers) are disadvantaged.
- **Key Insight**: Prioritize fixing no-JS validation (e.g., pre-submission checks) and optimizing load times.

### T4_delete (Delete)
- **Quantitative**: 100% completion in both modes with fast, consistent times. No errors recorded.
- **Inclusion Lens**: Seamless performance across environments, ensuring equal access for all users.
- **Key Insight**: Serves as a benchmark for other tasks; maintain this parity in future updates.

---

## Priority Findings

1. **T3_add No-JS Parity Failure**  
   - Evidence: 50% completion rate, 509% slower median time, and validation errors in no-JS mode.  
   - Impact: Excludes users who disable JS, violating WCAG 2.1.1 (Keyboard) parity requirements.  
   - Fix: Implement client-side validation for no-JS, optimize form submission flow.

2. **T2_edit Validation Feedback Gaps**  
   - Evidence: 50% error rate (blank title) in both modes, indicating unclear guidance.  
   - Impact: Confuses users with cognitive disabilities; screen readers may miss errors (WCAG 3.3.1 Error Identification).  
   - Fix: Add inline error messages, ARIA live regions, and explicit field labels.

3. **T3_add Performance in No-JS**  
   - Evidence: 3456ms median time (vs. 567ms in JS-On) suggests excessive page reloads.  
   - Impact: Frustrates users with slow connections, increasing abandonment risk.  
   - Fix: Minimize server round-trips, implement partial page updates for no-JS.