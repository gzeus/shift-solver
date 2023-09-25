# shift-solver

Small Spring Boot app for assigning employees to shifts in a given month.
Works with MySQL database, but has no Front-end GUI at the moment.
I'll try implementing thyme-leaf html templates in future versions.

Main functions:
- View, add, edit and delete employees at /api/employees 
- View, add, edit and delete individual shifts at /api/shifts
- View, add, edit and delete individual PTO at /api/holiday

- GET request at /api/shifts/{year}/{month} to display all existing shifts in a given month
- POST request at /api/shifts/{year}/{month} to generate shifts in a given month, based on the employees,
their holidays and other data in the POST JSON body (see below)


    Post request with following JSON body to generate shifts for the given month.
    ---- following values are all optional
    "publicHolidays" - Date[] (List of public holidays with no shifts)
    "weekdaysToExclude" - int[] (What weekdays have no shifts every week. 1 - Monday, 7 - Sunday)
    "recalculate" - boolean (if true, deletes all previous shifts in the month from the database and calculates again)

