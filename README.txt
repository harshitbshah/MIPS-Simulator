'On my honor, I have neither given nor received unauthorized aid on this assignment.'

STUDENT DETAILS
-------------------------

Name: Harshit Shah
Gator ID: 1211-6976
Email ID: hbshah@ufl.edu


ASSUMPTION
-------------------------
- Java is installed and configured in the machine where it is to be executed
- Input file should be a binary file (ie. .bin file)
- Output file must be a text file.


DESCRIPTION
-------------------------

In this project, I've implemented advanced pipeline using Tomasulo algorithm with out-of-order execution and in-order commit along with a Branch Predictor using the Branch Target Buffer for a processor which executes MIPS32 instructions which are mentioned below.
Basically, I implemented the Tomasulo Algorithm with two simplifications in handling store-load memory dependences:
1. The memory addresses for loads and stores are generated in order.
2. When a load is data dependent on an early store (with a matching address), the load stalls until the parent store commits and the data is stored into memory. If a match is found with multiple early stores, the parent is the latest store.

Only the following MIPS Instruction are supported by the MIPS Simulator:
> SW, LW
> J, BEQ, BNE, BGEZ, BGTZ, BLEZ, BLTZ
> ADDI, ADDIU
> BREAK
> SLT, SLTI , SLTU
> SLL, SRL, SRA
> SUB, SUBU, ADD, ADDU
> AND, OR, XOR, NOR
> NOP

INSTRUCTIONS
------------------------
- Compile the java file using 'make' command in Linux which should produce a .class file
- The command to execute the Disassembler class file should be of the following format:

java MIPSsim [input_bin_file_name] [output_text_file_name] [-Tm:n]

Where
> Inputfilename - The file name of the binary input file. (in this case: fibonacci_bin.bin which is already present in the folder)
> Outputfilename - The file name for printing the output.
> -Tm:n - Optional argument to specify the start (m) and end (n) cycles of simulation output trace.
> -T0:0 indicates that no tracing is to be performed (just print the final state); eliminating the argument specifies that every cycle (complete execution) is to be traced.

NOTE
------------------------
I've used the Final_output.txt file given in the announcement section on Canvas as my reference.
