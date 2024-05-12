import json
import sys
import os

def generate_interface(service_name, methods):
    # Define the content for the C# interface based on the service and methods
    interface_content = f"public interface {str(service_name)}\n"
    interface_content += "{\n"

    # Generate method signatures within the interface
    for method_name, param_types in methods.items():
        params_str = ", ".join(f"{param_type} param{i+1}" for i, param_type in enumerate(param_types))
        interface_content += f"    {method_name.split(':')[0]} {method_name.split(':')[1]}({params_str});\n"

    interface_content += "}\n"
    return interface_content

def generate_cs_files(services_data):
    path="orbInterfaces\\"
    isExist = os.path.exists(path)
    if not isExist:
            os.makedirs(path)
    # Iterate over each service and generate a .cs file for its interface
    for service_name, methods in services_data.items():
        interface_content = generate_interface(service_name, methods)

        #Write the interface content to a .cs file
        file_name = (path+f"{service_name}.cs")
        with open(file_name, "w") as f:
            f.write(interface_content)

        print(f"Generated C# interface file: {file_name}")

def main():
    # Load the input JSON file
    input_file_path = sys.argv[1]
    with open(input_file_path, "r") as json_file:
        services_data = json.load(json_file)

    # Validate the structure of the loaded JSON data
    if "services" not in services_data:
        print("Error: 'services' key not found in the input JSON file.")
        return

    # Generate .cs files for each service's interface
    generate_cs_files(services_data["services"])

if __name__ == "__main__":
    main()