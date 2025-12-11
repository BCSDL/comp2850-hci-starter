# Key Diffs — Code and UX Changes

**Purpose**: Document specific code/template/CSS changes made during Week 10 redesign.

**Format**: Show before/after snippets or annotated screenshots with brief explanations.

---

## Change 1: Task Delete Confirmation Accessibility

### Problem

Delete actions lacked confirmation prompts in no-JS mode, leading to accidental data loss (37% error rate in Week 9 pilots). Screen reader users couldn't anticipate the irreversible action, violating WCAG 3.3.4 (Error Prevention).

**Evidence**: Week 9 `05-findings.md` section B2 (Finding: "No confirmation for delete actions in no-JS mode")

---

### Code Changes

#### Before

**File**: `templates/tasks/index.peb`

```html
{% for task in tasks %}
  <div class="task-item">
    <span>{{ task.title }}</span>
    <a href="/tasks/{{ task.id }}/delete" class="delete-btn">Delete</a>
  </div>
{% endfor %}
```

**Issues**:
- ❌ Direct delete link with no confirmation (risk of accidental clicks)
- ❌ No ARIA warnings for screen reader users
- ❌ No-JS path lacks protective workflow

---

#### After

**File**: `templates/tasks/index.peb`

```html
{% for task in tasks %}
  <div class="task-item">
    <span>{{ task.title }}</span>
    <a href="/tasks/{{ task.id }}/confirm-delete" class="delete-btn" aria-haspopup="dialog">
      Delete
    </a>
  </div>
{% endfor %}

{# Confirmation dialog template #}
{% if showConfirmDialog %}
  <div id="delete-confirm" role="dialog" aria-modal="true" tabindex="-1">
    <h2>Are you sure?</h2>
    <p>This will permanently delete the task "{{ task.title }}".</p>
    <form action="/tasks/{{ task.id }}/delete" method="post">
      <button type="submit" class="danger">Confirm Delete</button>
      <a href="/tasks" class="cancel">Cancel</a>
    </form>
  </div>
{% endif %}
```

**Improvements**:
- ✅ Added intermediate confirmation page (no-JS) with dialog semantics
- ✅ `aria-haspopup="dialog"` warns SR users of upcoming confirmation
- ✅ `role="dialog" aria-modal="true"` identifies modal to assistive tech
- ✅ Explicit "Cancel" option prevents accidental actions

**WCAG compliance**: 
- ✅ **3.3.4 Error Prevention (AA)** — Destructive actions require confirmation
- ✅ **4.1.2 Name, Role, Value (A)** — Dialog properly labeled and role-assigned

---

#### Routes Changes

**File**: `src/main/kotlin/routes/Tasks.kt`

**Before**:
```kotlin
get("/tasks/{id}/delete") {
    val id = call.parameters["id"]?.toInt() ?: error("Invalid ID")
    taskRepository.delete(id)
    call.respondRedirect("/tasks")
}
```

**Issues**:
- ❌ Immediate deletion without user confirmation
- ❌ No opportunity to correct accidental clicks

---

**After**:
```kotlin
get("/tasks/{id}/confirm-delete") {
    val id = call.parameters["id"]?.toInt() ?: error("Invalid ID")
    val task = taskRepository.get(id) ?: error("Task not found")
    call.respondHtml(renderTemplate("tasks/index.peb", mapOf(
        "tasks" to taskRepository.all(),
        "showConfirmDialog" to true,
        "task" to task
    )))
}

post("/tasks/{id}/delete") {
    val id = call.parameters["id"]?.toInt() ?: error("Invalid ID")
    taskRepository.delete(id)
    call.respondRedirect("/tasks")
}
```

**Improvements**:
- ✅ Separated "confirm" and "execute" steps in no-JS flow
- ✅ GET route shows confirmation; POST route executes deletion
- ✅ Maintains task context in confirmation dialog

**WCAG compliance**:
- ✅ **3.2.2 On Input (A)** — Consistent behavior for delete actions

---

#### CSS Changes

**File**: `styles.css`

**Before**:
```css
.delete-btn {
  color: #dc3545; /* Contrast 4.0:1 (FAILS AA) */
  text-decoration: none;
}
```

**Issues**:
- ❌ Low contrast for critical action link
- ❌ No visual distinction from regular links

---

**After**:
```css
.delete-btn {
  color: #b02a37; /* Contrast 5.2:1 (PASSES AA) */
  font-weight: 600;
  padding: 0.25rem 0.5rem;
  border: 1px solid #b02a37;
  border-radius: 4px;
}

#delete-confirm {
  border: 2px solid #b02a37;
  padding: 1.5rem;
  background: white;
  max-width: 500px;
  margin: 2rem auto;
}

#delete-confirm h2 {
  color: #b02a37;
  margin-top: 0;
}

.danger {
  background: #b02a37;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  margin-right: 1rem;
}
```

**Improvements**:
- ✅ Increased contrast for delete links (5.2:1)
- ✅ Visual emphasis for destructive actions (border, weight)
- ✅ Modal dialog styled to stand out from page content
- ✅ Clear visual hierarchy in confirmation prompt

**WCAG compliance**:
- ✅ **1.4.3 Contrast (AA)** — Text contrast ≥ 4.5:1
- ✅ **1.4.11 Non-text Contrast (AA)** — UI components have ≥ 3:1 contrast

---

### UX Impact

**Before** (Week 9 pilots):
- Keyboard users accidentally tabbed to "Delete" and pressed Enter → task deleted without warning
- Screen reader users heard "Delete" but no context about permanence → 40% accidental deletions
- Low vision users missed the faint delete link → confusion about how to remove tasks

**After** (Week 10 re-pilots):
- All users encounter a confirmation step: 0% accidental deletions
- Screen reader users hear "Delete, popup dialog" → anticipate confirmation
- Low vision users easily identify delete actions and confirmation dialog via high contrast
- No-JS users have equivalent protection to HTMX mode (which uses a modal)

**Screenshots**:
- `05-evidence/before-delete-link.png` — Unstyled delete link with low contrast
- `05-evidence/after-confirm-dialog.png` — High-contrast confirmation dialog
- `05-evidence/sr-delete-announcement.png` — NVDA screen reader output showing dialog announcement

---

## Summary of Changes

**Total files modified**: 3

| File | Type | Lines Changed | Description |
|------|------|--------------|-------------|
| `templates/tasks/index.peb` | Pebble template | +28 / -5 | Added confirmation dialog and ARIA attributes |
| `src/main/kotlin/routes/Tasks.kt` | Kotlin | +15 / -4 | Added confirm-delete route and POST handler |
| `styles.css` | CSS | +22 / -3 | Improved contrast and styling for delete actions |

---

## Testing Evidence

**Regression tests**: See `02-a11y-regression-checklist.csv` (all PASS)

**Re-pilot data**: See `06-metrics/post/postchange.csv` (delete error rate reduced from 37% to 0%)

**Before/after comparison**: See `03-before-after-summary.md`

---

## Commit History

```
commit 9f2d3c7
Author: Jane Doe
Date: 2025-10-18

feat(a11y): add delete confirmation for accessibility (WCAG 3.3.4)

- Added confirmation dialog with ARIA roles
- Separated GET (confirm) and POST (execute) routes
- Improved contrast for delete actions (5.2:1)
- Tested with NVDA and keyboard-only: 0 accidental deletions
- Maintained no-JS parity with HTMX modal behavior

Closes: wk9-04 (see backlog/backlog.csv)
Related: WCAG 3.3.4 (Error Prevention AA), 1.4.3 (Contrast AA)
```

---

## Summary

**Total changes**: 3 files modified, +65 / -12 lines changed

**Key achievements**:
- ✅ 0% accidental deletions in re-pilots (down from 37%)
- ✅ Screen reader users receive clear action warnings
- ✅ Low vision users benefit from improved contrast and styling
- ✅ No-JS parity maintained with HTMX mode
- ✅ All delete workflows comply with WCAG 3.3.4

**WCAG compliance**: **AA** achieved (3.3.4, 4.1.2, 1.4.3, 1.4.11)

**Next steps**: Week 11 studio crit — demonstrate confirmation flow with diverse user scenarios

---

**Author**: Yiming Xu
**Date**: 2025-12-07
**Related files**: `01-redesign-brief.md`, `02-a11y-regression-checklist.csv`, `03-before-after-summary.md`
```