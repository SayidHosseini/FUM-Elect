docker logout
docker login

echo "Auth Microservice:"
cd ../AuthenticationService
if git pull | grep -q 'Already up-to-date'; then
	echo "  -> Already the latest!"
else
	echo "  -> Need to Rebuild!"
        docker build -t sayid/auth:arm .
        docker push sayid/auth:arm
fi

echo "ElectionUI Microservice:"
cd ../ElectionUIService
if git pull | grep -q 'Already up-to-date'; then
        echo "  -> Already the latest!"
else
        echo "  -> Need to Rebuild!"
        docker build -t sayid/election_ui:arm .
        docker push sayid/election_ui:arm
fi

docker logout
docker login

echo "ElectionManagerDB Microservice:"
cd ../ElectionManagerDb
if git pull | grep -q 'Already up-to-date'; then
        echo "  -> Already the latest!"
else
        echo "  -> Need to Rebuild!"
        docker build -t ardalanfp/election_manager_db:arm .
        docker push ardalanfp/election_manager_db:arm
fi

echo "ElectionPortalDB Microservice:"
cd ../ElectionPortalDb
if git pull | grep -q 'Already up-to-date'; then
        echo "  -> Already the latest!"
else
        echo "  -> Need to Rebuild!"
        docker build -t ardalanfp/election_portal_db:arm .
        docker push ardalanfp/election_portal_db:arm
fi

echo "ElectionManager Microservice:"
cd ../FUM_Election_ElectionManager
if git pull | grep -q 'Already up-to-date'; then
        echo "  -> Already the latest!"
else
        echo "  -> Need to Rebuild!"
        docker build -t ardalanfp/fum_election_electionmanager:arm .
        docker push ardalanfp/fum_election_electionmanager:arm
fi

echo "ElectionPortal Microservice:"
cd ../FUM_Election_ElectionPortal
if git pull | grep -q 'Already up-to-date'; then
        echo "  -> Already the latest!"
else
        echo "  -> Need to Rebuild!"
        docker build -t ardalanfp/fum_election_electionportal:arm .
        docker push ardalanfp/fum_election_electionportal:arm
fi

echo "\n\nStarting to Deploy"
cd ../FUM-Elect
docker stack deploy -c docker-compose.yml FUM-Elect
