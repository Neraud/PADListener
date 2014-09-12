#!/bin/bash

#############################################################################################
# Utility script to update data from PADherder without needing to export it from a device   #
# It's not used by the app itself, it's just for convenience                                #
#############################################################################################

ROOT_SCRIPT_DIR=$(dirname "$(readlink -f ${BASH_SOURCE[0]})")
ASSSETS_DIR=${ROOT_SCRIPT_DIR}"/PADListenerApp/src/main/assets"

PADHERDER_ROOT_URL="https://www.padherder.com"
PADHERDER_ROOT_API_URL=${PADHERDER_ROOT_URL}"/api"

function updateMonsterInformation() {
    echo "=================================================="
    echo "Fetching monster information"
    wget -O "${ASSSETS_DIR}/monsters.json.new" --no-check-certificate "${PADHERDER_ROOT_API_URL}/monsters/"
    oldCkSum=$(cksum "${ASSSETS_DIR}/monsters.json" | cut -d" " -f1)
    newCkSum=$(cksum "${ASSSETS_DIR}/monsters.json.new" | cut -d" " -f1)

    if [ "$oldCkSum" != "$newCkSum" ] ; then
        echo "Updating new JSON"
        mv "${ASSSETS_DIR}/monsters.json.new" "${ASSSETS_DIR}/monsters.json"
        date "+%Y-%m-%d" > "${ASSSETS_DIR}/monsters.date"
    else
        echo "JSON was already up to date"
        rm "${ASSSETS_DIR}/monsters.json.new"
    fi
    echo "=================================================="
}

function updateMonsterEvolution() {
    echo "=================================================="
    echo "Fetching monster evolution"
    wget -O "${ASSSETS_DIR}/evolutions.json.new" --no-check-certificate "${PADHERDER_ROOT_API_URL}/evolutions/"
    oldCkSum=$(cksum "${ASSSETS_DIR}/evolutions.json" | cut -d" " -f1)
    newCkSum=$(cksum "${ASSSETS_DIR}/evolutions.json.new" | cut -d" " -f1)

    if [ "$oldCkSum" != "$newCkSum" ] ; then
        echo "Updating new JSON"
        mv "${ASSSETS_DIR}/evolutions.json.new" "${ASSSETS_DIR}/evolutions.json"
        date "+%Y-%m-%d" > "${ASSSETS_DIR}/monsters.date"
    else
        echo "JSON was already up to date"
        rm "${ASSSETS_DIR}/evolutions.json.new"
    fi
    echo "=================================================="
}

function updateMonsterImages() {
    echo "=================================================="
    echo "Fetching images"
    cat "${ASSSETS_DIR}/monsters.json" \
        | sed -e 's/{/\n/g' -e 's/^\[//g' -e 's/]$//g' -e 's/}[,[]//g' \
        | awk -F ',"' '
        {
           id = "";
           urlImage = "";
           for(i=1 ; i<=NF ; i++) {
                gsub("\"", "", $i)
                split($i,a,":")
                if(a[1] == "id") {
                    id = a[2];
                } else if(a[1] == "image60_href") {
                    urlImage = a[2];
                }
            }

            if(id != "" && urlImage != "") {
                print id "=" urlImage;
            }
        }
        ' \
        | while read line ; do
            monsterId=$(echo $line | cut -d"=" -f1)
            imageUrlRel=$(echo $line | cut -d"=" -f2)
            imageUrlFull=${PADHERDER_ROOT_URL}${imageUrlRel}

            wget -q -O "${ASSSETS_DIR}/monster_images/${monsterId}.png.new" --no-check-certificate "${imageUrlFull}"

            if [ -f "${ASSSETS_DIR}/monster_images/${monsterId}.png" ] ; then
                oldCkSum=$(cksum "${ASSSETS_DIR}/monster_images/${monsterId}.png" | cut -d" " -f1)
                newCkSum=$(cksum "${ASSSETS_DIR}/monster_images/${monsterId}.png.new" | cut -d" " -f1)

                if [ "$oldCkSum" != "$newCkSum" ] ; then
                    echo " - updating image for $monsterId"
                    mv "${ASSSETS_DIR}/monster_images/${monsterId}.png.new" "${ASSSETS_DIR}/monster_images/${monsterId}.png"
                else
                    echo " - image already up to date for $monsterId"
                    rm "${ASSSETS_DIR}/monster_images/${monsterId}.png.new"
                fi
            else
                echo " - adding image for $monsterId"
                mv "${ASSSETS_DIR}/monster_images/${monsterId}.png.new" "${ASSSETS_DIR}/monster_images/${monsterId}.png"
            fi
        done

    echo "=================================================="
}


updateMonsterInformation
updateMonsterEvolution
updateMonsterImages
