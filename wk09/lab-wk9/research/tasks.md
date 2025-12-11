# Evaluation Tasks for Peer Pilots

## Task 1: Filter Tasks by Keyword
**Scenario**: You need to find all tasks related to "project deadline" to check their status.  
**Steps**:  
1. Navigate to the task list page.  
2. Use the search/filter feature to look for "project deadline".  
3. Verify that only matching tasks are displayed.  

**Success Criteria**:  
- All tasks containing "project deadline" in the title are visible.  
- Non-matching tasks are hidden.  
- Completed in ≤ 30 seconds (JS-on) or ≤ 45 seconds (JS-off).  


## Task 2: Edit a Task Inline
**Scenario**: You notice a typo in a task titled "Finish math homework" (should be "Finish physics homework").  
**Steps**:  
1. Locate the task in the list.  
2. Enter edit mode for the task.  
3. Correct the title to "Finish physics homework".  
4. Save the changes.  

**Success Criteria**:  
- Task title is updated correctly.  
- No other task details (e.g., completion status) are altered.  
- Screen readers announce the update (if using JS-on).  


## Task 3: Add a High-Priority Task
**Scenario**: You remember an upcoming exam and need to add "Study for COMP2850 exam" with high priority.  
**Steps**:  
1. Find the "Add New Task" section.  
2. Enter the task title: "Study for COMP2850 exam".  
3. Mark it as high priority (if applicable).  
4. Submit the task.  

**Success Criteria**:  
- New task appears in the list.  
- Priority is correctly marked (if visible).  
- Form validation works (e.g., blocks empty titles).  


## Task 4: Delete a Completed Task
**Scenario**: You finished "Buy groceries" and want to remove it from the list.  
**Steps**:  
1. Locate the completed task "Buy groceries".  
2. Use the delete function (button/link).  
3. Confirm deletion (if prompted).  

**Success Criteria**:  
- Task is removed from the list.  
- No other tasks are deleted.  
- JS-off mode: Confirmation page works without errors.  


**Inclusion Focus**:  
- All tasks must be completable with keyboard-only navigation.  
- Screen reader users should receive clear feedback for each action.  
- Tasks work in both JS-on (HTMX) and JS-off modes.