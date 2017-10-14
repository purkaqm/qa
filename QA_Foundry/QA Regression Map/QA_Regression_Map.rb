# /*************************************************************
# * PSS QA regression map.
# * Create a chart based on issues per component in the given branch.
# * It will also list all the approved issues in the branch which do not
# * have a corresponding test in the milestone regression.
# *
# * Version : 1.4
# * Changes :
# *
# *  01-Jul-2017 1.4 : Can query just one field now. And map only if there is some data.
# *                     User approved issues are now optional.
# *  11-May-2017 1.3 : Normalization limit uses a floor now.
# *  06-Jan-2017 1.2 : QA library functions are now made use.
# *  29-Dec-2016 1.1 : Corrected save_issue_info to save extracted info itself and not whole structure.
# *  28-Dec-2016 1.0 : Can find out which JIRA issues in the given release are not 
# *                         in the corresponding TestRail milestone.
# *  28-Dec-2016 0.6 : Added function to get issues not in regression milestone (TestRail).
# *                     Refactored for DRY.
# *  27-Dec-2016 0.5 : Added function to get user approved issues in current version.
# *                     save_issue_info() now handles non-iterators.
# *                     User approved issues can be queried now.
# *                     Fields if not found will be silently omitted now.
# *  26-Dec-2016 0.4 : Added a very basic normalization.
# *                     Added support for latest Google chart API.
# *  25-Dec-2016 0.3 : Added draw map feature, uses Google charts (Old API).
# *  24-Dec-2016 0.2 : Refactored for DRY.
# *  10-Dec-2016 0.1 : Created.
# *
# * CopyRight :
# * All rights reserved.
# **************************************************************/

require_relative './../QA_Functions/QA_Lib_Io'
require_relative './../QA_Functions/QA_Lib_Net'

require 'rest-client'   # REST
require 'base64'        # Encoding authorization
require 'gchart'        # Google charts
require 'nokogiri'      # HTML parser


require 'pry'

$DEBUG_RUN = true


# JIRA configurations #

# The base 
$base_url = "https://uplandsoftware.atlassian.net/rest/api/latest"

# JQL parameters for regression
# It would look like - 
# project = PS AND issuetype in standardIssueTypes() AND status = "QA Approved" AND  ORDER BY created DESC, priority DESC, updated DESC
#
$base_query = 'project = PS AND issuetype in standardIssueTypes() AND status = "QA Approved"'
$order_by = 'ORDER BY created DESC, priority DESC, updated DESC'
version = '17'

# Parameters, what all we are interested in
#
# Issue (Main type)
# key
# fields.components[0].name
#
# Project fields - 
# https://uplandsoftware.atlassian.net/rest/api/latest/issue/createmeta?expand=projects.issuetypes.fields
#
# fields.issuetype.name
# fields.labels
# fields.customfield_12000 - Customer
# fields.customfield_11603.value - Discovered by
# fields.customfield_11300 - Epic link
#
$issue_fields = [
    "fields.components.name",
]

# Sub-tasks
#   key
#   fields.parent.key
#   fields.versions[0].name
#   fields.assignee.name
#   fields.components[0].name
#   fields.fixVersions - May or may not be populated
#

# \remarks Set the Google chart APIs to use.
# There are two options. 
# 1. Image chart API.
# 2. Chart API.
# Chart API is latest and image APIs are deprecated.
# But here, using Chart API instead of image API will generate
# HTML files instead of PNG files.
# 
$image_chart_api = 'false' # Control data so type is string

user = 'currentUser()'


# JIRA Domain #
def run_command_search(base_url, auth_code, query, start_index = 0)
    command = base_url + "/search"
    headers = {
                :Authorization => "Basic #{auth_code}",
                :params => {:jql => query, :startAt => start_index}
    }
    
    list = perform_get(command, headers)
    
end

def get_issue_info(list, paramas)
    # \remarks Be wary of the issue types.
    # When mixed type, make sure the field-parameters are present in both.
    # Otherwise this will simply omit that field when not present.
 
    details = Hash.new
    
    # For every issue in the list
    issues = list["issues"]
    issues.each do |issue|
    
        # Fresh details
        issue_details = []
        
        # For every parameter of interest
        paramas.each do |param|
            
            # Start again with issue once a new parameter is got
            field = issue
            param_name = param
            param = param.split(".")
            
            # Extract data from the list #            
            # Traverse the fields
            param.each do |value|
                if field.class == Array
                    field.each do |f|
                        if (f.has_key?(value) )
                            field = f["#{value}"]
                            
                            # \remarks If this is the last parameter-field then store 
                            # that value. As we are interested in its value.
                            # So, given 
                            #   a.b.c, c will be last field in the parameter.
                            #   a then that is the last parameter field.
                            #
                            if param.last == value then issue_details.push( "#{param_name} = #{field}" ) end
                        end
                    end
                else
                    if (field.has_key?(value) )
                        field = field["#{value}"]
                        if param.last == value then issue_details.push( "#{param_name} = #{field}" ) end
                    end
                end
            end # Traverse
        end # All parameters
        
        # Store the details under issue ID
        details[ issue["key"] ] = issue_details
        
    end # All issues
    
    return(details)
end

def save_issue_info(issue_details, file_out)
    # The input issue_details contains details as per the batches.
    # It is an array with length equal to the total batches.
    
    # For every batch of details
    issue_details.each do |details|
        # {
        #   <Issue-Id> => [ <Issue-Id>, <Component-1>, <Component-2> ],
        #   <Issue-Id> => [ <Issue-Id>, <Component-1> ]
        # }
        #
        if ( details.respond_to?("each") )
            details.each do |key, value|
                file_out.puts key
                file_out.puts value
                file_out.puts "\n"
            end # All hash entries
        else
            # Not an iterator, simply output
            file_out.puts details
        end            
    end # All batches
end

def get_issues_in_batches(query, issue_fields, message)
    details_list = []
    
    auth_code = $auth_code
    base_url = $base_url    
    
    list = run_command_search(base_url, auth_code, query)
    if (list.length > 0)
        
        start_index = list["startAt"].to_i
        batch_size = list["maxResults"].to_i
        end_index = start_index + batch_size
        total = list["total"].to_i
        
        message = sprintf("Found total of #{total} issues %s\n", message)
        print_log(message)
        
        # Log to the file the first batch
        details = get_issue_info(list, issue_fields)
        details_list.push(details)
        print_log( "\tProcessed #{end_index} issues of #{total}\n" )
    
        start_index = end_index
        while (start_index < total)            
            # Get remaining batches
            list = run_command_search(base_url, auth_code, query, start_index)                
            
            end_index = start_index + batch_size
            end_index = (end_index > total) ? total : end_index
            start_index = end_index
            
            details = get_issue_info(list, issue_fields)
            details_list.push(details)
            print_log( "\tProcessed #{end_index} issues of #{total}\n" )
        end
        
    end
   
   return (details_list)
   
end

def get_approved_issues(version)
    # Get all of QA approved issues.
    # JIRA at a time returns maximum 50 issues. This number is is set by JIRA admin.
    # So we will have to query the issues in batches if needed.
    #
    
    details_list = []
    base_url = $base_url
    base_query = $base_query
    order_by = $order_by
    issue_fields = $issue_fields
    
    # Get a list of QA approved JIRA issues for the given release
    if (version != "")
        query = "#{base_query} AND fixVersion = \"#{version}\" #{order_by}"

        details_list = get_issues_in_batches(query, issue_fields, "under version = #{version}")
        
    else
        print_log("No version specified\n")
    end
    
    return (details_list)
end

def get_map_data(issue_details, data_type)
    # Given the type, get the map data
    
    map_data = Hash.new
    
    issue_details.each do |details|
        details.each do |key, value|
            # From the value extract type we are interested in
            value.each do |v|
                if v.include?(data_type)
                    # Get the value from "<type> = <value>"
                    v = v.split("=")
                    data = v.last.strip
                    
                    # Calculate its distribution
                    if map_data.has_key?(data)
                        # If the data is already present as key then increment its value
                        map_data[data] += 1
                    else
                        # Otherwise add its entry
                        map_data[data] = 1
                    end
                    
                end # Found data type
            end # For every value
        end # For every issue
    end # For every batch
    
    return (map_data)
end

def normalize_map_data(map_data)
    # \remarks Normalize the data. If we can call that.
    # Consider 5 modules. But we can handle only 2. Thus 
    # we need to know which are 2 most significant ones.
    #
    # For now, simply consider all of those components which have 
    # at least five or more issues.
    
    # \todo Need a better way to handle the normalization of data.

    norm_map_data = Hash.new
    
    norm_map_data["Others"] = 0
    
    min = map_data.values.min
    max = map_data.values.max
    total = map_data.keys.length
    limit = min.to_f / max * total
    limit = limit.floor
    
    map_data.each do |key, value|
        if value < limit
            norm_map_data["Others"] += 1
        else
            norm_map_data[key] = value
        end
    end
    
    # \remarks Make the data ordered.
    # Take apart the hash, sort and put it back.
    # This will help in charting.
    norm_map_data = norm_map_data.sort_by {|k,v| v}.reverse
    norm_map_data = norm_map_data.to_h
    
    return (norm_map_data)
end

def get_google_chart_script(map_data, field)
        script_str = "
        <!--Load the AJAX API-->
        <script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script>
        <script type='text/javascript'>
        
          // Load the Visualization API and the corechart package.
          google.charts.load('current', {'packages':['corechart']});

          // Set a callback to run when the Google Visualization API is loaded.
          google.charts.setOnLoadCallback(drawChart);

          // Callback that creates and populates a data table,
          // instantiates the pie chart, passes in the data and
          // draws it.
          function drawChart() {

            // Create the data table.
            var data = new google.visualization.DataTable();
            data.addColumn('string', '#{field}');
            data.addColumn('number', 'Slices');
            data.addRows(#{map_data.to_a});

            // Set chart options
            var options = {'title':'#{field}',
                           'width':800,
                           'height':600};

            // Instantiate and draw our chart, passing in some options.
            var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
            chart.draw(data, options);
          }
          
        </script>        
        "
end

def get_google_chart_body()
    body_str = "
    <body>
        <!--Div that will hold the pie chart-->
        <div id='chart_div'></div>
    </body>
    "
end

def create_chart(map_data, field, file_prefix)
   if $image_chart_api == 'true'
        # Image chart API    
        pie_chart = Gchart.new(
                        :type => "pie",
                        :size => "600x400",
                        :data => map_data.values,
                        #:legend => map_data.keys,
                        :labels => map_data.keys,
                        :title => "#{field}",
                        :filename => "#{file_prefix}_#{field}.png"
                    )
        pie_chart.file
        
    else
        # Latest Google chart API - Uses JS
        page = Nokogiri::HTML('')
        
        html = page.create_element("html")
        page.add_child(html)
        
        head = page.create_element("head")
        html.add_child(head)
        
        script = Nokogiri::HTML.fragment( get_google_chart_script(map_data, field) )
        head.add_child(script)
        
        body = Nokogiri::HTML.fragment( get_google_chart_body() )
        html.add_child(body)
        
        #puts page
        html_file = File.open("#{file_prefix}_#{field}.html", "w")
        html_file.puts page
        html_file.close
    
    end # $image_chart_api
end

def create_regression_map(all_approved_issues, file_out)

    # all_approved_issues = [{
        # "PS-1" => ["fields.components.name = Dashboard"],
        # "PS-2" => ["fields.components.name = Dashboard", "fields.components.name = Metrics"],
    # }] 
    $issue_fields.each do |field|
        map_data = get_map_data(all_approved_issues, field)
        
        if map_data.length > 0
            map_data = normalize_map_data(map_data)
        
            #save_issue_info(map_data, file_out)
            create_chart(map_data, field, file_out.path)
        end

    end
end

def get_user_approved_issues(username)
    # Get all issues approved by the given user.
    
    approved_list = []
    
    # On query, issues returned can be 
    # 1. Subtasks or standard issues or whatever.
    # 2. Belong to any version (Fix Versions).
    # 3. Open items are not present.
    #
    # Output, only the standard type issues. 
    # That means, parent of subtask for subtask.
    #    
    query = "project = PS AND status in ('QA Approved', Done) AND assignee in (#{username}) ORDER BY key DESC"
    issue_fields = [ "fields.parent.key", ]

    # Can contains both sub-tasks and standard issue types
    details_list = get_issues_in_batches(query, issue_fields, "approved by #{username}")
    
    # Derive standard issue types
    details_list.each do |batch|
        batch.each do |issue, details|
            is_subtask = false
            
            details.each do |field|
                if field.include?("parent")
                    is_subtask = true
                    
                    # Extract the parent
                    parent = field.split("=").last.strip
                    approved_list.push(parent)
                end
            end # All fields
            
            if !is_subtask then approved_list.push(issue) end
            
        end # All issues in a batch
        
    end # End of all batches
    
    return (approved_list)
end

def get_user_current_issues(all_current_issues, user_approved_issues)
    # Given all the issues in current version and all user approved issues,
    # return issues approved by the user for the current version.
    
    # Type is array of hashes
    # [ {}, {}, ]
    all_current_issues_name = []
    all_current_issues.each do |batch|
        batch.each do |issue, details|
            all_current_issues_name.push(issue)
        end
    end
    
    all_user_approved_issues_name = user_approved_issues.flatten
    
    # The intersection of two sets of issues is what we are looking for.
    # So that an issue is user approved and belongs to the current version.
    all_user_approved_issues_name & all_current_issues_name
end

def get_issues_not_in_regression(all_approved_issues, issues_in_regression)
    
    # Extract issue keys from all issues approved
    all_issues = []
    all_approved_issues.each do |batch|
        all_issues.push(batch.keys)
    end    
    all_issues = all_issues.flatten
    
    # Now get a difference of two sets
    issues_not_in_regression = all_issues - issues_in_regression
end

# TestRail Domain #
def get_regression_test_cases(version)
    project_id = 1
    auth_code = get_auth_code("TR")
    base_url = "https://upland.testrail.com/index.php?/api/v2/"
    commands = [
        ["get_milestones/", {:is_completed => 0} ],
        ["get_plans/", {:is_completed => 0, :milestone_id => 0} ],
        ["get_plan/", {} ],
        ["get_tests/", {} ],
    ]
    headers = {
                :Authorization => "Basic #{auth_code}",
                :content_type => "application/json",
    } 
    
    # First get all open milestones
    command_index = 0
    command = "#{base_url}#{commands[command_index][0]}#{project_id}"
    params = commands[command_index][1]
    headers[:params] = params
    
    list = perform_get(command, headers)
    
    # Extract the milestone ID for the current version
    milestone_id = -1
    list.each do |milestone|
        if milestone["name"].include?(version) 
            milestone_id = milestone["id"] 
            break 
        end
    end   
    
    # Get test plans under the milestone
    command_index = 1
    command = "#{base_url}#{commands[command_index][0]}#{project_id}"
    params = commands[command_index][1]
    params[:milestone_id] = milestone_id
    headers[:params] = params
    
    list = perform_get(command, headers)
    
    # Extract all test plans
    test_plans_id = []
    list.each do |test_plan|
        test_plans_id.push(test_plan["id"])
    end
    
    # Get all the test run in the test plan
    print_log("\nTrying to get all test runs under v#{version} milestone\n")
    test_plans_id = test_plans_id.sort
    
    command_index = 2
    params = commands[command_index][1]    
    headers[:params] = params
    
    test_runs_id = []
    total_tests = 0
    
    test_plans_id.each do |test_plan_id|        
        command = "#{base_url}#{commands[command_index][0]}#{test_plan_id}"       
        list = perform_get(command, headers)
        
        entries = list["entries"]
        entries.each do |entry|
            test_runs = entry["runs"]
            test_runs.each do |test_run|
                test_runs_id.push(test_run["id"])
                total_tests += test_run["passed_count"] + test_run["blocked_count"] + 
                                test_run["untested_count"] + test_run["retest_count"] + 
                                test_run["failed_count"]
            end
        end
    end
    print_log("\tFound a total of #{total_tests} tests\n")
    
    print_log("\nTrying to fetch #{total_tests} tests\n")
    
    # Get all the tests in the current milestone
    test_cases = Hash.new
    
    command_index = 3
    params = commands[command_index][1]    
    headers[:params] = params
    
    test_runs_id.each do |test_run_id|
        command = "#{base_url}#{commands[command_index][0]}#{test_run_id}"       
        list = perform_get(command, headers)
        
        list.each do |test|
            test_cases[test["id"]] = ["case = #{test["case_id"]}", "issues = #{test["refs"]}"]            
        end
    end
    print_log("\tFetched #{test_cases.length} tests\n")
    
    return (test_cases)

end

def get_regression_issues(version)
    # Get all the JIRA issues referenced by the tests (cases)
    ref_issues = Hash.new    
    
    test_cases = get_regression_test_cases(version)
    
    test_cases.each do |test, details|
        issues = details[1].split("=").last.strip
        issues = issues.split(",")
        issues.each do |issue|
            issue = issue.strip.upcase
            if ref_issues.has_key?(issue)
                # Update the tests issue is attached to
                ref_issues[issue] = ref_issues[issue].push(test)
            else
                # Add a new entry
                ref_issues[issue] = [test]
            end
        end
    end
    
    return (ref_issues.keys)
end

if (ARGV.length < 1)
    print_log("\nUsage: <script> <control_file>\n")
    exit()
elsif (ARGV[0] != "")
    control_fname = ARGV[0]
    fname = get_output_filename(control_fname)
    
    # Read control information
    control_data = get_control_data(control_fname)
    # If valid control information is found then use that
    if control_data.has_key?("version") then version = control_data["version"] end
    if control_data.has_key?("field") then $issue_fields = control_data["field"] end
    if control_data.has_key?("image_api") then $image_chart_api = control_data["image_api"] end
    if control_data.has_key?("user") then user = control_data["user"] end

    # The $issue_fields must always be an array. So, convert the variable to array.
    # Otherwise, Array#each will throw exception. Which is what is used everywhere.    
    if $issue_fields.class != Array
        $issue_fields = [$issue_fields, "nil"]
    end
    
    # Open file for writing
    fname = version + "_" + fname
    file_out = File.open(fname, "w")
    
    start_time = Time.now
    
    # Get all QA approved issues
    $auth_code = get_auth_code("JIRA")
    print_log("\nTrying to get all QA Approved issues in v#{version} from JIRA\n", file_out)
    all_approved_issues = get_approved_issues(version)
    #save_issue_info(all_approved_issues, file_out)    
    
    # Create a map of the data
    print_log("\nCreating maps from v#{version} QA Approved issues\n")
    create_regression_map(all_approved_issues, file_out)

    if (user != 'nil')
        # Get all issues approved by the given user    
        print_log("\nTrying to get all issues approved by #{user} from JIRA\n", file_out)
        user_approved_issues = get_user_approved_issues(user)
        save_issue_info(user_approved_issues, file_out)
    
        # Now find out which issues are approved by the user in current version
        print_log("\nDeriving all issues approved by #{user} in v#{version}\n", file_out)
        user_current_issues = get_user_current_issues(all_approved_issues, user_approved_issues)
        save_issue_info(user_current_issues, file_out)
        print_log("\tFound #{user_current_issues.length} issues approved by #{user} in v#{version}\n")
    end
    
    # Get all the issues in the current regression
    issues_in_regression = get_regression_issues(version)
    
    # Now figure out which approved issues are not in regression (Not created or added)
    print_log("\nDeriving all approved issues not in regression milestone\n", file_out)
    issues_not_in_regression = get_issues_not_in_regression(all_approved_issues, issues_in_regression)
    save_issue_info(issues_not_in_regression, file_out)
    print_log("\tFound #{issues_not_in_regression.length} approved issues not in v#{version} regression \n")
    
    # If JIRA issue has test cases attached then additionally we can add them to regression
    
    end_time = Time.now
    print_log("\nTotal time taken = #{end_time - start_time}s\n")

    # Clean up
    file_out.close

    print_log("\n*** Done ***\n")

end

# \todo 
# Get only the total number of issues specified by user? For example first 10 out of 170 or 100 of 170.


# End of file #