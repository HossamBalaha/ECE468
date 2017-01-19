#/bin/bash

echo -e "\n"
echo -e "factorial2\n"
java -cp lib/antlr.jar:classes/ Micro testcases/inputs/factorial2.micro > test.out
./tiny4R testcases/outputs/factorial2.out < testcases/inputs/factorial2.input > result.txt
head -n1 result.txt
./tiny4R test.out < testcases/inputs/factorial2.input > result.txt
head -n1 result.txt

echo -e "\n"
echo -e "fma\n"
java -cp lib/antlr.jar:classes/ Micro testcases/inputs/fma.micro > test.out
./tiny4R testcases/outputs/fma.out < testcases/inputs/fma.input > result.txt
head -n1 result.txt
./tiny4R test.out < testcases/inputs/fma.input > result.txt
head -n1 result.txt

echo -e "\n"
echo -e "step4_testcase\n"
java -cp lib/antlr.jar:classes/ Micro testcases/inputs/step4_testcase.micro > test.out
./tiny4R testcases/outputs/step4_testcase.out < testcases/inputs/step4_testcase.input > result.txt
head -n1 result.txt
./tiny4R test.out < testcases/inputs/step4_testcase.input > result.txt
head -n1 result.txt

echo -e "\n"
echo -e "step4_testcase2\n"
java -cp lib/antlr.jar:classes/ Micro testcases/inputs/step4_testcase2.micro > test.out
./tiny4R testcases/outputs/step4_testcase2.out < testcases/inputs/step4_testcase2.input > result.txt
head -n1 result.txt
./tiny4R test.out < testcases/inputs/step4_testcase2.input > result.txt
head -n1 result.txt

echo -e "\n"
echo -e "step4_testcase3\n"
java -cp lib/antlr.jar:classes/ Micro testcases/inputs/step4_testcase3.micro > test.out
./tiny4R testcases/outputs/step4_testcase3.out < testcases/inputs/step4_testcase3.input > result.txt
head -n1 result.txt
./tiny4R test.out < testcases/inputs/step4_testcase3.input > result.txt
head -n1 result.txt

echo -e "\n"
echo -e "test_adv\n"
java -cp lib/antlr.jar:classes/ Micro testcases/inputs/test_adv.micro > test.out
./tiny4R testcases/outputs/test_adv.out < testcases/inputs/test_adv.input > result.txt
head -n1 result.txt
./tiny4R test.out < testcases/inputs/test_adv.input > result.txt
head -n1 result.txt

echo -e "\n"
echo -e "test_expr\n"
java -cp lib/antlr.jar:classes/ Micro testcases/inputs/test_expr.micro > test.out
./tiny4R testcases/outputs/test_expr.out < testcases/inputs/test_expr.input > result.txt
head -n1 result.txt
./tiny4R test.out < testcases/inputs/test_expr.input > result.txt
head -n1 result.txt

echo -e "\n"
echo -e "test_if\n"
java -cp lib/antlr.jar:classes/ Micro testcases/inputs/test_if.micro > test.out
./tiny4R testcases/outputs/test_if.out < testcases/inputs/test_if.input > result.txt
head -n1 result.txt
./tiny4R test.out < testcases/inputs/test_if.input > result.txt
head -n1 result.txt

echo -e "\n"
echo -e "test_dowhile\n"
java -cp lib/antlr.jar:classes/ Micro testcases/inputs/test_dowhile.micro > test.out
./tiny4R testcases/outputs/test_dowhile.out < testcases/inputs/test_dowhile.input > result.txt
head -n1 result.txt
./tiny4R test.out < testcases/inputs/test_dowhile.input > result.txt
head -n1 result.txt

echo -e "\n"
echo -e "fibonacci2\n"
java -cp lib/antlr.jar:classes/ Micro testcases/inputs/fibonacci2.micro > test.out
./tiny4R testcases/outputs/fibonacci2.out < testcases/inputs/fibonacci2.input > result.txt
head -n15 result.txt
./tiny4R test.out < testcases/inputs/fibonacci2.input > result.txt
head -n15 result.txt