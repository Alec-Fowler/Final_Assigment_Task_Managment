# TASK MANAGEMENT (FINAL ASSIGNMENT)

## Features
- All Tasks can be saved and automatically load into a file the default one will be created on first use
- Save Function 
  - Menu = (File → Save) 
  - Key = (CTRL + S)
- Save As function 
  - Menu = (File → SaveAs) 
  - Key = (CTRL + SHIFT + S)
- Load Function
  - Menu = (File → Load)
  - Key = (CTRL + L)
- Task Addition 
  - Menu = ( Edit → New)
  - Key = (N)
- Task Editing (Edit)
  - Menu = (Edit → Edit)
  - Key = (E)
- Task Deleting (Delete)
  - Menu = (Edit → Delete) 
  - Key = (Del)
- Clean Table Design 5 sortable categories 
- (id: int, name: String, priority: enum,  status: enum ,  due_date: String, description: String )
- Date validation
- Error messages
- 
## Windows
- New Task Window
  - All Editable
  - Creates new task at the next index available
- Edit Task Window 
  - Everything is editable excluding ID
  - Edits task selected by index of table item selected
- Task View Window
  - Double-Click a Table Item and bring up the task View Item
  - You can go forward and backward through the list 
  - Nothing is editable in the list
  
# Technical
- Built with Swing
- Data Saved as JSON using Jackson Library (using maven)

# Future needs before public release
- More accessibility options for all windows
- Tab through all entries and menus