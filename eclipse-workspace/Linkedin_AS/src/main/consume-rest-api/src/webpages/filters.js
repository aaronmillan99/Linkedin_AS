import React, { useState } from 'react';
import useSound from 'use-sound';
import mySound from '../sounds/notification.mp3';
import "../App.css"


const Filters = () => {
   const [job, setJob] = useState("");
   const [location, setLocation] = useState("");
   const [time, setTime] = useState("");
   const [linkjobs, setlinkjobs] = useState([]);
   const [jobssaved, setJobsSaved] = useState([]);

   const [searchState, setSearchState] = useState("Search Stopped");
   const [filtersState, setFiltersState] = useState("Hide Filters");
   const [searching, setSearching] = useState(false);
   const [disableInputs, setDisableInputs] = useState(false);
   const [refreshIntervalId, setRefreshIntervalId] = useState(0);
   const [clickedJobDescription, setClickedJobDescription] = useState("");
   var allJobOffers = [];

   const [playSound] = useSound(mySound);



    function sendFilters(){

        //validations
        let val = checkValidaciones();

        if(val===true){
            //Multiply the value *60 to pass it to seconds
            let timeInSeconds = fixTimeSearchSeconds();

            let url = "http://localhost:8080/api/get/"+job+"+"+location+"+"+timeInSeconds;
            fetch(url)
            .then((response) => response.json())
            .then((data) => {
                //extract array from object
                const array = data.key;
                
                if(!arraysEqual(allJobOffers,array) && array.length!==0){
                    playSound();
                }
                allJobOffers = allJobOffers.concat(array);
                setlinkjobs(allJobOffers);
                console.log(allJobOffers);

            })
            .catch((err) => {
                console.log(err.message);
            });
        }
    }

    function checkValidaciones(){
        let res = true;
        if(job.length===0){
            res = false;
        }else if(location.length===0){
            res = false;
        }else if(time.length===0){
            res = false;
        }
        return res;
    }    

    function arraysEqual(a1,a2) {
        /* WARNING: arrays must not contain {objects} or behavior may be undefined */
        return JSON.stringify(a1)===JSON.stringify(a2);
    }
    

    function fixTimeSearchSeconds(){
        let seconds = parseInt(time, 10);
        seconds = seconds * 60;
        return seconds.toString();
    }

    function intervalSendFilters(){
        //save value in a new variable to force the update of it when doing the set
        const searchingBool = !searching;
        setSearching(searchingBool);
        if(searchingBool===true){
            //disable button and input
            setDisableInputs(true);

            //change searching text            
            setSearchState("Searching...")
            //toggles
            toggleFilters();

            sendFilters();
            setRefreshIntervalId(setInterval(sendFilters, 20000));
        }else{
            //unable button and input            
            setDisableInputs(false);
            //kill interval
            clearInterval(refreshIntervalId);
            //toggles
            toggleFilters();

            //change searching text                        
            setSearchState("Search Stopped");
        }
    }

    function clearResults(){
        setlinkjobs([]);
        allJobOffers= [];
    }

    function toggleFilters() {
        var x = document.getElementById("filters");
        if (x.style.display === "none") {
          x.style.display = "block";
          setFiltersState("Hide Filters")
        } else {
          x.style.display = "none";
          setFiltersState("Show Filters")

        }
      }

    //   function toggleDescription() {
    //     var x = document.getElementById("description-container");
    //     if (x.style.display === "none") {
    //       x.style.display = "block";
    //       setFiltersState("Hide Filters")
    //     } else {
    //       x.style.display = "none";
    //       setFiltersState("Show Filters")

    //     }
    //   }

    
      function deleteOfferFound(element) {
        var x = linkjobs.indexOf(element);
        const copy = JSON.parse(JSON.stringify(linkjobs));
        copy.splice(x,1);
        setlinkjobs(copy);
      }

      function deleteOfferFoundSaved(element) {
        var x = jobssaved.indexOf(element);
        const copy = JSON.parse(JSON.stringify(jobssaved));
        copy.splice(x,1);
        setJobsSaved(copy);
      }

      function saveJobOffer (element) {
        deleteOfferFound(element);
        const copy = JSON.parse(JSON.stringify(jobssaved));
        copy.push(element);
        setJobsSaved(copy);
      }
   
    return(
        <div id="filters-container-main">
            <div id="buttons-container">
                <div id="buttons-filter">
                    <button className='button-40' onClick={toggleFilters}>{filtersState}</button>
                    <button className='button-40' onClick={ () => {intervalSendFilters();}}>{searchState}</button>
                    <button className='button-40' onClick={clearResults} disabled={disableInputs}>Clear Results</button>  

                    <div id="filters">
                        <div>
                            <input type="text" placeholder="Job Name" value={job} onInput={e => setJob(e.target.value)} disabled={disableInputs}></input>
                        </div>    
                        <div>
                            <input type="text" placeholder="Location" value={location} onInput={e => setLocation(e.target.value)} disabled={disableInputs}></input>
                        </div>  
                        <div>
                            <input type="text" placeholder="Time (in minutes)" value={time} onInput={e => setTime(e.target.value)} disabled={disableInputs}></input>
                        </div>   
                    </div>
                </div>
                <div id="button-offers">
                    <button className='button-40' style={{marginLeft: "10em"}}>Job Offers:</button>
                    {linkjobs.map(item => <div style={{marginLeft: "8rem"}}><div id="results"  onClick={() => {setClickedJobDescription(item.jobdetailsdto.description);}}><a key={item.jobId} href={item.detailsURL} target="_blank" rel="noreferrer"><div>{item.jobdetailsdto.tittle}</div></a></div><div><button disabled={disableInputs} onClick={() => { saveJobOffer(item) }}>Save</button><button disabled={disableInputs} onClick={() => { deleteOfferFound(item) }}>Delete</button></div></div>)} 

                </div>
                <div id="button-saved-offers">
                    <button className='button-40' style={{marginLeft: "5rem"}} >Saved Offers:</button>
                    {jobssaved.map(item => <div style={{marginLeft: "5rem"}}><div id="results"><a key={item.jobId} href={item.detailsURL} target="_blank" rel="noreferrer"><div>{item.jobdetailsdto.tittle} {item.jobdetailsdto.description}</div></a></div><div><button onClick={() => { deleteOfferFoundSaved(item) }}>Delete</button></div></div>)}
                </div>
            </div>
            {/* <div id="description-container">
                {clickedJobDescription}
            </div> */}

        </div>    
    )
}

export default Filters;