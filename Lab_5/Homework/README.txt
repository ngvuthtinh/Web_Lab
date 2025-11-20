STUDENT INFORMATION:
Name: Nguyễn Vũ Thành Tính
Student ID: ITCSIU23039
Class: Monday Morning 1-4

COMPLETED EXERCISES:
[x] Exercise 5: Search
[x] Exercise 6: Validation
[x] Exercise 7: Sorting & Filtering
[x] Exercise 8: Pagination
[x] Bonus 1: Export Excel
[x] Bonus 3: Combined Search + Filter + Sort + Pagination

MVC COMPONENTS:
- Model: Student.java
- DAO: StudentDAO.java
- Controller: StudentController.java, ExportServlet.java
- Views: student-list.jsp, student-form.jsp

FEATURES IMPLEMENTED:
- All CRUD operations (Create, Read, Update, Delete)
- Search functionality (by name, code, email)
- Server-side validation (Regex for code/email, required fields)
- Sorting by all columns (ID, Code, Name, Email, Major) with toggling order (ASC/DESC)
- Filter by Major
- Pagination (navigate through large lists)
- Combined logic: Can Search, Filter, Sort, and Paginate simultaneously.

KNOWN ISSUES:
- Duplicate checking for Student Code and Email relies on Database Constraints (SQL Error) rather than a pre-check in Java. The error message for duplicates might be generic.
- Database credentials (username/password) are hardcoded in StudentDAO.java. Ideally, these should be moved to a configuration file or environment variables for better security.

EXTRA FEATURES:
- Export to Excel: Download student list as .xlsx file using Apache POI library.
- Advanced UI: Added sort indicators, active page highlighting, and consistent button styling.
- State Preservation: Search keywords and filter selections remain active after sorting or changing pages.

TIME SPENT:  Around 10 hours