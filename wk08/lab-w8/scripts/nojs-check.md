# No-JS Parity Verification Script — Week 8

**Purpose**: Verify all task flows work identically with JavaScript disabled, ensuring progressive enhancement and accessibility compliance.

**Setup**:
1. Open browser DevTools (F12 / Ctrl+Shift+I)
2. Disable JavaScript:
   - Chrome: Settings (⚙️) → Debugger → Check "Disable JavaScript"
   - Firefox: about:config → Search "javascript.enabled" → Set to false
3. Hard refresh the page (Ctrl+Shift+R / Cmd+Shift+R) to clear cached JavaScript
4. Navigate to `http://localhost:8080/tasks` to start testing


## Test 1: Add Task (Happy Path)

**Steps**:
1. Locate the "Add New Task" form at the top of the page
2. Enter title "No-JS test task" in the input field
3. Click the "Add Task" button

**Expected**:
- Full page reload occurs (verify in Network tab: only HTML response)
- New task "No-JS test task" appears in the task list
- URL remains `http://localhost:8080/tasks` (follows PRG pattern)
- No error messages are displayed

**Evidence**: Screenshot of task list with "No-JS test task" visible. Save to `evidence/wk8/nojs-parity/test1-success.png`.

**Result**: [ ] Pass  [ ] Fail


## Test 2: Add Task (Validation Error)

**Steps**:
1. Leave the title input field empty
2. Click the "Add Task" button

**Expected**:
- Full page reload occurs
- URL updates to `http://localhost:8080/tasks?error=title`
- Error summary appears at the top of the page with text: "There is a problem: Title is required. Please enter at least one character."
- Error summary includes a link to the title input (e.g., `<a href="#title">Title is required</a>`)
- Title input has `aria-invalid="true"` attribute (verify in DevTools)

**Evidence**: Screenshot of error summary and highlighted input. Save to `evidence/wk8/nojs-parity/test2-error.png`.

**Result**: [ ] Pass  [ ] Fail


## Test 3: Filter Tasks

**Steps**:
1. Enter "test" in the filter input field
2. Press Enter or click the "Filter" button

**Expected**:
- Full page reload occurs
- URL updates to `http://localhost:8080/tasks?q=test`
- Only tasks containing "test" in their title are displayed
- Result count updates to reflect filtered results (e.g., "3 tasks found")
- Filter input retains the value "test"

**Evidence**: Screenshot of filtered task list. Save to `evidence/wk8/nojs-parity/test3-filter.png`.

**Result**: [ ] Pass  [ ] Fail


## Test 4: Pagination

**Steps**:
1. Ensure there are at least 11 tasks (add more if needed)
2. Click the "2" link in the pagination controls

**Expected**:
- Full page reload occurs
- URL updates to `http://localhost:8080/tasks?page=2`
- Second page of tasks is displayed
- Pagination link for page 2 has `aria-current="page"` attribute (verify in DevTools)
- "Previous" link is enabled (if on page 2 or higher)

**Evidence**: Screenshot of page 2 with active pagination indicator. Save to `evidence/wk8/nojs-parity/test4-pagination.png`.

**Result**: [ ] Pass  [ ] Fail


## Test 5: Edit Task

**Steps**:
1. Click the "Edit" link for the task "No-JS test task"
2. In the edit form, change the title to "No-JS edited task"
3. Click the "Save" button

**Expected**:
- Full page reload occurs
- Task list shows updated title "No-JS edited task"
- URL redirects to `http://localhost:8080/tasks` (follows PRG pattern)
- Success message appears (e.g., "Task updated successfully")

**Evidence**: Screenshot of updated task in list. Save to `evidence/wk8/nojs-parity/test5-edit-success.png`.

**Result**: [ ] Pass  [ ] Fail


## Test 6: Delete Task

**Steps**:
1. Locate the task "No-JS edited task"
2. Click the "Delete" button/link for this task

**Expected**:
- Full page reload occurs
- Task "No-JS edited task" is removed from the list
- URL remains `http://localhost:8080/tasks`
- Success message appears (e.g., "Task deleted successfully")

**Evidence**: Screenshot of task list with the task removed. Save to `evidence/wk8/nojs-parity/test6-delete.png`.

**Result**: [ ] Pass  [ ] Fail


## Test 7: Keyboard Navigation

**Steps**:
1. Use the Tab key to navigate through all interactive elements:
   - Skip link (if present) → Filter input → "Filter" button → Task edit/delete links → Pagination links → Add task input → "Add Task" button
2. Verify focus is visible at each stop
3. Activate the "Add Task" button using the Enter key
4. Navigate to an error link (from Test 2) using Tab, then activate with Enter

**Expected**:
- Focus order matches visual layout
- All interactive elements are reachable via Tab
- Enter key activates buttons and links
- Focus is visible on all elements (minimum 3:1 contrast ratio)
- Activating an error link moves focus to the associated input

**Evidence**: Notes on tab order and screenshot of visible focus on a pagination link. Save to `evidence/wk8/nojs-parity/test7-keyboard.png`.

**Result**: [ ] Pass  [ ] Fail


## Test 8: Browser History

**Steps**:
1. Start at `http://localhost:8080/tasks`
2. Add a new task (e.g., "History test") → Page reloads
3. Filter for "history" → Page reloads to `http://localhost:8080/tasks?q=history`
4. Click page 2 in pagination → Page reloads to `http://localhost:8080/tasks?q=history&page=2`
5. Click the browser's Back button twice

**Expected**:
- First Back: Returns to `http://localhost:8080/tasks?q=history` (filtered, page 1)
- Second Back: Returns to `http://localhost:8080/tasks` (no filter)
- Task list state updates correctly with each history change

**Evidence**: Notes on history behavior (screenshots of URL bar at each step). Save to `evidence/wk8/nojs-parity/test8-history.txt`.

**Result**: [ ] Pass  [ ] Fail


## Summary

**Passed**: _____ / 8  
**Failed**: _____ / 8  

**Issues found**: (log in `backlog/backlog.csv` with IDs wk8-XX)  
- Example: wk8-03,8,medium,functionality,"No success message after no-JS edit",,open  

**Notes**:
- All screenshots must include visible URL bar to confirm state
- Retest failed cases after fixes
- Compare results with JS-enabled tests for parity

---

**Verified by**: __________  
**Date**: __________