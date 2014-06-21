#!/system/bin/sh

##################################################
# Expected variables
##################################################
CHAIN_NAME_PREFIX="$1"


##################################################
# Variables check
##################################################

if [ "$CHAIN_NAME_PREFIX" == "" ] ; then
	echo "CHAIN_NAME_PREFIX is mandatory !"
	exit 11
fi

CHAIN_NAME_1=$CHAIN_NAME_PREFIX
CHAIN_NAME_2=$CHAIN_NAME_PREFIX"_OUTPUT"


##################################################
# Init
##################################################
echo "Disabling IPTables redirection for "
echo " - Chain name prefix : "$CHAIN_NAME_PREFIX
echo " -> Chain name 1 : "$CHAIN_NAME_1
echo " -> Chain name 2 : "$CHAIN_NAME_2
EXIT_CODE=0

##################################################
# Removing Chains
##################################################
echo "Removing Chain 1"
iptables -D INPUT -j $CHAIN_NAME_1
if [ $? -ne 0 ] ; then echo "Error !" && EXIT_CODE=1 ; fi

echo "Removing Chain 1 nat"
iptables -t nat -D PREROUTING -j $CHAIN_NAME_1
if [ $? -ne 0 ] ; then echo "Error !" && EXIT_CODE=1 ; fi

echo "Removing Chain 2 nat"
iptables -t nat -D OUTPUT -j $CHAIN_NAME_2
if [ $? -ne 0 ] ; then echo "Error !" && EXIT_CODE=1 ; fi


##################################################
# Removing Rules
##################################################
echo "Removing rules for Chain 1"
iptables --flush $CHAIN_NAME_1
if [ $? -ne 0 ] ; then echo "Error !" && EXIT_CODE=1 ; fi

echo "Removing rules for Chain 1 nat"
iptables -t nat --flush $CHAIN_NAME_1
if [ $? -ne 0 ] ; then echo "Error !" && EXIT_CODE=1 ; fi

echo "Removing rules for Chain 1 nat"
iptables -t nat --flush $CHAIN_NAME_2
if [ $? -ne 0 ] ; then echo "Error !" && EXIT_CODE=1 ; fi


##################################################
# Deleting Chains
##################################################
echo "Deleting Chain 1"
iptables --delete-chain $CHAIN_NAME_1
if [ $? -ne 0 ] ; then echo "Error !" && EXIT_CODE=1 ; fi

echo "Deleting Chain 1 nat"
iptables -t nat --delete-chain $CHAIN_NAME_1
if [ $? -ne 0 ] ; then echo "Error !" && EXIT_CODE=1 ; fi

echo "Deleting Chain 2 nat"
iptables -t nat --delete-chain $CHAIN_NAME_2
if [ $? -ne 0 ] ; then echo "Error !" && EXIT_CODE=1 ; fi



echo "Finished !"
exit $EXIT_CODE
