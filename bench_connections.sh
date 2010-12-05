SERVER=192.168.2.11

#for i in 1
#do
#    echo "Running test with message size $i"
#    sbt "run junction://${SERVER}/testsession#xmpp 100 ${i} data/result_xmpp_connections_${1}.out"
#done

#for i in 1
#do
#    echo "Running test with message size $i"
#    sbt "run junction://${SERVER}:6667/testsession#irc 100 ${i} data/result_irc_connections_${1}.out"
#done

for i in 1
do
    echo "Running test with message size $i"
    sbt "run junction://${SERVER}/testsession#jx 100 ${i} data/result_jx_connections_${1}.out"
done

#for i in 1
#do
#    echo "Running test with message size $i"
#    sbt "run junction://192.168.2.15/testsession#jx 100 ${i} data/result_jx_mobile_connections_${1}.out"
#done

