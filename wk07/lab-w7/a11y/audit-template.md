# Accessibility Audit — Week 7

## Scope
Pages: Tasks index, Edit form (inline), Create form (full page). JS on/off.

## Keyboard
- [✅] All actions possible with keyboard only (Add, Edit, Save, Cancel, Delete accessible via Tab/Enter/Space)
- [⚠️] Focus order logical; no traps; visible focus (Order is logical, no traps, but default focus outline is faint)

## Structure & Semantics
- [✅] One H1, meaningful H2/H3 (H1: "Task Manager"; H2: "Current Tasks")
- [✅] Landmarks present (main, nav if any) (`<main id="main">` wraps content)
- [✅] Lists and buttons/links used correctly (Tasks in `<ul>`, actions as `<button>` not `<div>`)

## Forms
- [✅] Labels bound to inputs (`<label for="title">` linked to input `id="title"`)
- [✅] Errors linked via aria-describedby (Error messages use `aria-describedby="error-msg"`)
- [⚠️] Instructions/hints near controls (Basic hints present but could be more descriptive)

## Dynamic Updates
- [✅] Status messages announced (`role="status"`) (Success/error messages use `role="status"`)
- [✅] No unexpected focus shifts (Focus stays on updated task after save)
- [✅] OOB updates don’t hide essential content (Status messages append without hiding tasks)

## Contrast & Zoom
- [❌] AA contrast met (Delete button text #6c757d on white: 4.2:1 < 4.5:1)
- [✅] 200% zoom still works (Layout reflows without horizontal scroll)

## Findings
| ID | Location | Issue | Impact | Inclusion risk | Evidence | Fix idea |
|----|----------|-------|--------|----------------|---------|---------|
| A1 | Delete button (`<button type="submit">Delete</button>`) | Insufficient color contrast (1.4.3 AA) | High | Low vision, elderly | WebAIM check: #6c757d on #ffffff = 4.2:1 (fails AA) | Change text color to #495057 (7:1 contrast) |
| A2 | All interactive elements | Focus visible insufficient (2.4.7 AA) | Medium | Keyboard-only, motor impairments | Default outline faint; hard to track focus | Add custom CSS: `:focus { outline: 3px solid #1976d2; }` |
| A3 | Edit form | Hint text lacks detail | Low | Cognitive disabilities | Hint only says "Task title" | Expand to "Enter task title (at least 1 character)" |
| A4 | Delete action | No confirmation step | Medium | Motor, cognitive | Accidental click deletes task immediately | Add `hx-confirm="Delete this task?"` to delete button |
| A5 | Error messages (no-JS) | Not announced by screen reader | High | Screen reader users | No `role="alert"` in redirect-based errors | Add error summary with `role="alert"` on edit page |