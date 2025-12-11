# Prototyping Constraints & Trade-offs

## Rendering splits
- Full page: `/tasks` returns layout + list + pager. Serves as the server-first baseline, functioning without JavaScript.
- Fragment: `/tasks/fragment` returns list + pager + OOB status. Used for HTMX-enhanced updates, reducing data transfer by only sending changing parts.

## URL & History
- `hx-push-url="true"` on filter and pager links keeps Back/Forward meaningful. Ensures URLs reflect current filter and page state, supporting bookmarking and sharing.
- Query parameters (`?q=search&page=2`) maintain state across both HTMX and no-JS paths.

## Accessibility hooks
- Live region `#status` announces changes (e.g., "Found X tasks") via `hx-swap-oob`.
- Result count associated with list via `aria-describedby="result-count"` for screen reader context.
- Pagination links use `aria-current="page"` to indicate active page.
- Skip link in header becomes visible on focus, enabling keyboard users to bypass navigation.
- Form elements include proper labels and `aria-describedby` for input hints.

## State management decisions
- Query parameter naming: `q` for filter query, `page` for pagination to keep URLs intuitive.
- Page size: 10 items per page, balancing server load (fewer database queries) and client cognitive load (manageable scroll).
- Shared state between full page and fragments: Ensures consistent filtering/pagination across both paths using the same query parameters.

## Performance notes
- Page size: 10 items; consider server time vs client cost. Smaller pages reduce initial load but increase pagination interactions.
- Fragments avoid re-sending the full layout, reducing bandwidth by ~70% compared to full page reloads.
- HTMX triggers for filter use `delay:300ms` to reduce server requests from rapid keystrokes.

## Future risks
- Template duplication between full page and fragments. Risk of divergent markup if `_list` and `_pager` partials aren't maintained consistently.
- Focus management after deletes (ensure next focusable target). Keyboard focus may become unpredictable without explicit handling.
- Divergence between HTMX and no-JS paths if not tested regularly, leading to inconsistent functionality.
- Potential 404 on out-of-range pages if pagination bounds aren't validated server-side.

## Dual-Path Architecture Trade-offs
### Benefits
- **Inclusion**: Works for all users regardless of JavaScript availability.
- **Resilience**: Graceful degradation if JS fails to load.
- **Testing**: Baseline path validates server-first architecture robustness.
- **Progressive enhancement**: Accessible core with layered improvements via HTMX.

### Costs
- **Code complexity**: Routes require conditional logic to handle both full page and fragment responses.
- **Response duplication**: Must generate consistent output for both paths using shared partials.
- **Testing burden**: Each feature requires verification with and without JavaScript.

## Accessibility verification
- **Keyboard-only**: Tab order is logical; focus remains visible on filter form, pagination links, and tasks. Skip link functions correctly.
- **Screen reader**: Result count announcements trigger on filter/pagination changes. `aria-current` for active page is properly announced.
- **No-JS parity**: Filtering and pagination work via full-page reloads when JavaScript is disabled. URLs update correctly to reflect state.
- **History**: Back/forward buttons revert filter/pagination state as expected, thanks to `hx-push-url`.