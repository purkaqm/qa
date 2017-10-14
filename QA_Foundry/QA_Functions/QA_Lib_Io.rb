# /*************************************************************
# * PSS QA - Helper functions library - IO
# * Version : 1.2
# * Changes :
# *
# *  06-Jan-2017 1.2 : Added read control data function.
# *  23-Dec-2016 1.1 : Added log function.
# *  17-Dec-2016 1.0 : Created.
# *
# * CopyRight :
# * All rights reserved.
# **************************************************************/

require 'io/console'    # Password echo off
require 'base64'        # Encoding authorization


# Get user credentials
def get_credentials(message = "")
    printf("\n%s\n", message)
    printf("%s", "User:")
    user = $stdin.readline().strip
    printf("%s", "Password:")
    password = STDIN.noecho(&:gets).strip
    printf("\n\n")
    
    return ([user, password])
end

def get_auth_code(message = "")
    if $DEBUG_RUN
        if message.include?("JIRA") then auth_code = "" end
        if message.include?("TR") then auth_code = "" end
        auth_code
    else
        credentials = get_credentials(message)
        auth_code = Base64.encode64("#{credentials[0]}:#{credentials[1]}")
    end
end

def get_output_filename(input_name = "")
    if $DEBUG_RUN
        return ("log.txt")
    end
    
    # \remarks Windows does not allow ':' in the file name.
    timestamp = Time.now.strftime("%Y-%b-%d_%I_%M_%S_%p") # YYYY-MMM-DD_HH_MM_SS_?M
    
    name = "__out_#{timestamp}.txt"
    
    if input_name != ""
        name = input_name.split(".", 2)[0]
        name = "#{name}_#{timestamp}.txt"
    end
    
    return (name)
end

def print_log(message, file_out = nil)
    printf("%s", message)
    
    # If file is specified then write to it
    if (file_out != nil)
        file_out.puts message
    end    
end

def get_control_data(input_filename)
    # Read control information
    control_data = Hash.new
    
    open(input_filename, "r").each do |line|
        line = line.strip()
        
        # Not a comment then process it
        if !line.start_with?("#") && line != ""
            if (line.include?("=") )
                line = line.split("=", 2)
                param_name = line[0].strip.downcase
                param_value = line[1].strip
                
                if !control_data.has_key?(param_name)
                    control_data[param_name] = param_value
                else
                    # Key is already present, in which case, append the value
                    control_data[param_name] = [control_data[param_name]].push(param_value)
                end
            end
        end
        
    end
    
    return (control_data)
end


# End of file #
