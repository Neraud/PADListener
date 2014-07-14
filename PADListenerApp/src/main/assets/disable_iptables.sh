#!/system/bin/sh

##################################################
# Expected variables
##################################################
IPTABLES_EXECUTABLE_DIR="$1"
CHAIN_NAME_PREFIX="$2"

##################################################
# Variables check
##################################################

IPTABLES=$IPTABLES_EXECUTABLE_DIR"/iptables"

if [ ! -x "$IPTABLES" ] ; then
	echo "iptables ($IPTABLES) does not exist or is not executable !"
	exit 11
fi

if [ "$CHAIN_NAME_PREFIX" == "" ] ; then
	echo "CHAIN_NAME_PREFIX is mandatory !"
	exit 12
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
$IPTABLES -D INPUT -j $CHAIN_NAME_1
if [ $? -ne 0 ] ; then echo "Error removing Chain 1 !" && EXIT_CODE=1 ; fi

echo "Removing Chain 1 nat"
$IPTABLES -t nat -D PREROUTING -j $CHAIN_NAME_1
if [ $? -ne 0 ] ; then echo "Error removing Chain 1 nat !" && EXIT_CODE=1 ; fi

echo "Removing Chain 2 nat"
$IPTABLES -t nat -D OUTPUT -j $CHAIN_NAME_2
if [ $? -ne 0 ] ; then echo "Error removing Chain 2 nat !" && EXIT_CODE=1 ; fi


##################################################
# Removing Rules
##################################################
echo "Removing rules for Chain 1"
$IPTABLES --flush $CHAIN_NAME_1
if [ $? -ne 0 ] ; then echo "Error removing rules for Chain 1 !" && EXIT_CODE=1 ; fi

echo "Removing rules for Chain 1 nat"
$IPTABLES -t nat --flush $CHAIN_NAME_1
if [ $? -ne 0 ] ; then echo "Error removing rules for Chain 1 nat !" && EXIT_CODE=1 ; fi

echo "Removing rules for Chain 1 nat"
$IPTABLES -t nat --flush $CHAIN_NAME_2
if [ $? -ne 0 ] ; then echo "Error removing rules for Chain 1 nat !" && EXIT_CODE=1 ; fi


##################################################
# Deleting Chains
##################################################
echo "Deleting Chain 1"
$IPTABLES --delete-chain $CHAIN_NAME_1
if [ $? -ne 0 ] ; then echo "Error deleting Chain  !" && EXIT_CODE=1 ; fi

echo "Deleting Chain 1 nat"
$IPTABLES -t nat --delete-chain $CHAIN_NAME_1
if [ $? -ne 0 ] ; then echo "Error deleting Chain 1 nat !" && EXIT_CODE=1 ; fi

echo "Deleting Chain 2 nat"
$IPTABLES -t nat --delete-chain $CHAIN_NAME_2
if [ $? -ne 0 ] ; then echo "Error deleting Chain 2 nat !" && EXIT_CODE=1 ; fi


echo "Finished"
exit $EXIT_CODE
