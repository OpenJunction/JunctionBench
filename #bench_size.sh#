SERVER=192.168.2.11

#for i in 1 10 100 200 500 1000 1500 2000 3000 5000 10000 20000 50000 100000
#do
#    echo "Running test with message size $i"
#    sbt "run junction://${SERVER}/testsession#xmpp 100 ${i} data/result_xmpp_msg_len_${i}.out"
#done


#for i in 1 10 100 200 500 1000 1500 2000 3000 5000 10000 20000
#do
#    echo "Running test with message size $i"
#    sbt "run junction://${SERVER}:6667/testsession#irc 100 ${i} data/result_irc_msg_len_${i}.out"
#done

for i in 1 10 100 200 500 1000 1500 2000 3000 5000 10000 20000 50000 100000
do
    echo "Running test with message size $i"
    sbt "run junction://${SERVER}/testsession#jx 100 ${i} data/result_jx_msg_len_${i}.out"
done


#for i in 1 10 100 200 500 1000 1500 2000 3000 5000 10000 20000
#do
#    echo "Running test with message size $i"
#    sbt "run junction://192.168.2.15/testsession#jx 100 ${i} data/result_jx_mobile_msg_len_${i}.out"
#done



