-----------------------------------------------------------
QA Foundry
-----------------------------------------------------------

QA_Functions
----------------------
This is the core library on which most other tools depend on. It provides functions 
for authentication and calling REST APIs. The functions are written in Ruby.


QA Links Update
----------------------
Use this tool to update Confluence wiki page - PS QA Machines (ORD) with latest
information from the QA servers. For example, Build and Contexts (Among others).
That will update Important QA Links wiki page.
To run, run Run_QA_Links_update batch file.


QA Regression Map
----------------------
Use this tool to create a regression map. It can create a pie chart of number of issues
found or worked per components in the given branch (Fix version). You can also use this
to list the issues which have not been added to the milestone regression plan.
To run, run Run_Regression_Map batch file.


QA Automation Map
----------------------
Use this tool to create automation map. It will extract the TestRail test case references 
from test scripts. It runs on the local files. So you should have downloaded the scripts
from the Git repository first. 
To run, run Run_Automation_Map batch file.


QA Automation Coverage
----------------------
Use this tool to gather automation coverage data. The output will contain percent complete
status of each test suite in TestRail. The formatting leans towards Excel friendly.
To run, run Run_Automation_Coverage batch file.

