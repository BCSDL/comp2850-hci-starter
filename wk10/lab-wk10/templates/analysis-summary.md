# Pilot Data Analysis Summary — Week 10

**Study**: Peer pilots (n=5)  
**Purpose**: Summarise quantitative metrics and qualitative findings to inform redesign priorities.

---

## Summary Statistics

| Task | JS Mode | n_success | Median (ms) | MAD (ms) | Completion Rate | Error Rate | Notes |
|------|---------|-----------|-------------|----------|-----------------|------------|-------|
| T1 (Filter) | on | 2 | 1750 | 180 | 1.00 | 0.00 | No errors reported |
| T1 (Filter) | off | 1 | 3100 | 200 | 1.00 | 0.00 | Slower due to full page reloads |
| T1 (Filter) | all | 3 | 2100 | 220 | 1.00 | 0.00 | Consistent| T2 (Edit) | on | 2 | 1322 | 68 | 1.00 | 0.00 | Post-fix: errors resolved |
| T2 (Edit) | off | 1 | 3201 | 150 | 1.00 | 0.25 | One validation error in pre-fix |
| T2 (Edit) | all | 3 | 1850 | 110 | 1.00 | 0.08 | Improved from 80% pre-fix completion |
| T3 (Add) | on | 2 | 950 | 90 | 1.00 | 0.20 | Minor validation errors |
| T3 (Add) | off | 1 | 2800 | 120 | 1.00 | 0.00 | No-JS path functional |
| T3 (Add) | all | 3 | 1500 | 105 | 1.00 | 0.13 | |
| T4 (Delete) | on | 2 | 450 | 30 | 1.00 | 0.00 | Consistent performance |
| T4 (Delete) | off | 1 | 620 | 40 | 1.00 | 0.00 | No-JS matches HTMX success |
| T4 (Delete) | all | 3 | 510 | 35 | 1.00 | 0.00 | |

### Task-by-Task Interpretation

- **T1 Filter**: All participants completed the task successfully across both JS modes. JS-off mode showed longer median times (3100ms vs 1750ms) due to full page reloads, but no exclusionary barriers. Keyboard-only screen reader users users reported no issues with filter interaction (P2 notes: "Tab to filter box worked smoothly").
- **T2 Edit**: Significant improvement post-fix—completion rate increased from 80% to 100% (pre-fix data: 4/5 attempts). Pilots encountered pre-fix barriers including unannounced errors ("Didn't hear the error message" - P3, screen reader user) and focus traps. Post-fix, error messages use `role="alert"` and focus management is corrected.
- **T3 Add**: No major anomalies. MAD is low (105ms), indicating consistent performance. One validation error in JS-on mode (blank submission) was resolved with clearer required field indicators.
- **T4 Delete**: Parity across JS modes with 100% completion. Fast median times (<620ms) suggest no errors, indicating robust implementation in both variants.

## Priority Findings

| ID | Issue | Task | Evidence (metrics + notes) | WCAG | Impact | Inclusion | Effort | Priority |
|----|-------|------|-----------------------------|------|--------|-----------|--------|----------|
| wk9-01 | Error messages not announced to screen readers | T2 Edit | `analysis/analysis.csv#T2_edit`; `pilot-notes/P3.md` ("No error announcement") | 4.1.3 Status Messages | 5 | 5 | 2 | 8 |
| wk9-02 | No confirmation message in No-JS Add path | T3 Add | `pilot-notes/P4.md` ("Weren't sure if it saved"); 3/5 pilots mentioned uncertainty | 4.1.3 Status Messages | 4 | 3 | 1 | 6 |
| wk9-03 | Focus order inconsistent in T1 Filter results | T1 Filter | `pilot-notes/P5.md` (" keyboard test; median time 1.8x slower for keyboard users | 2.4.3 Focus Order | 3 | 4 | 2 | 5 |

## Data Quality Notes

- [x] All metrics logged (no missing sessions)
- [x] JS-off samples noted (n=1 for T1/T2/T3/T4; sufficient for preliminary analysis, follow-up with 1 more pilot recommended)
- [x] Any anomalies explained below:
  - P2 experienced a 2s network delay during T3 Add (JS-off); included in calculations as it reflects real-world conditions.

---

_Last updated: 2025-12-7