# Metrics CSV Schema

## File: `data/metrics.csv`
- **Purpose**: Track task performance and errors for Week 9 pilots.  
- **Format**: Comma-separated values (UTF-8).  
- **Privacy**: No PII (anonymous session IDs only).  


## Column Definitions
| Column       | Description                                                                 | Example                                  |
|--------------|-----------------------------------------------------------------------------|------------------------------------------|
| `ts_iso`     | UTC timestamp in ISO 8601 format (when event occurred).                     | `2024-11-05T14:35:22Z`                   |
| `session_id` | Anonymous 6-12 character hex ID (persists per pilot session).              | `P2_f7a3d`                               |
| `request_id` | Unique per-request ID (for tracing individual actions).                    | `req_b9c2e`                              |
| `task_code`  | Task identifier (matches `wk09/research/tasks.md`).                         | `T1_filter`, `T2_edit`                   |
| `step`       | Task step (e.g., `submit`, `validation`, `confirm`).                        | `submit`                                 |
| `outcome`    | Result of the step (`success`, `validation_error`, `timeout`).              | `validation_error`                       |
| `ms`         | Duration in milliseconds (server-side processing time).                     | `120` (200ms)                            |
| `http_status`| HTTP response code (200 = success, 400 = client error).                     | `400`                                    |
| `js_mode`    | Whether JavaScript was enabled (`js-on`) or disabled (`js-off`).            | `js-off`                                 |


## Example Rows
```csv
ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode
2024-11-05T14:35:22Z,P1_a3f7,req_001,T1_filter,submit,success,85,200,js-on
2024-11-05T14:36:10Z,P2_b8c4,req_002,T3_add,validation,validation_error,0,400,js-off
2024-11-05T14:37:00Z,P1_a3f7,req_003,T4_delete,submit,success,60,200,js-on