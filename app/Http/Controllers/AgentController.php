<?php

namespace App\Http\Controllers;

use App\Models\Agent;
use Illuminate\Http\Request;
use App\Http\Resources\AgentCollection;
use Illuminate\Support\Facades\Validator;

class AgentController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        //
        $data = Agent::all();
        if($data->count() != 0 ){
            return new AgentCollection($data);
        }
        return response()->json([
            "message"=>"Ressource not found",
        ],400);
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        //
    }

    public function auth_agent(Request $request){
        $validator = Validator::make($request->all(),[
            "code" =>'required|string',
        ]);
        if($validator->stopOnFirstFailure()->fails()){
            return response()->json([
                'message' => $validator->errors()
             ],402);
        }
        $field = $validator->validated();
        $data = Agent::where('code',strtolower($field['code']))->first() ;
        if(!$data){
            return response()->json([
               'message' => 'Utilisateur non valide'
            ],404);
        }
        return response()->json([
            'message' => 'login success',
            'client' => $data,
         ],200);
    }
    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(),[
            'nom'               => 'required|string',
            'postnom'           => 'string',
            'prenom'            => 'required|string',
            'genre'             => 'string',
            'telephone'         => 'required|string',
            'address'           => 'string',
            'date_naissance'    => 'string',
            'code'              => 'string'
        ]);

        if(!$validator->stopOnFirstFailure()->fails()) {
            $validated = $validator->validated();
            $code =$this->code_initial_agent . count(Agent::all()) + 1;
            $state = Agent::where("nom",$validated['nom'])->where("postnom",$validated['postnom'])->where("prenom",$validated['prenom'])->get();
            if(count($state) == 0){
                $agent = Agent::updateOrCreate([
                    'code'              => strtolower($code),
                    'nom'               => $validated['nom'],
                    'postnom'           => $validated['postnom'],
                    'prenom'            => $validated['prenom'],
                    'address'           => $validated['adresse']??"",
                    'genre'             => $validated['genre'],
                    'telephone'         => $validated['telephone']??"",
                    'date_naissance'    => $validated['date_naissance']??"",
                    ]);
                if($agent){
                    return response()->json([
                        "agent" => $agent,
                        "message"=>"Compte Agent créer avec succès",
                    ],201);
                }
            }
            return response()->json([
                "message"=>"Ce compte est déjà enregistrer",
            ],403);
            
        }
        return response()->json([
            "message"=>$validator->errors(),
        ],400);
    }

    /**
     * Display the specified resource.
     */
    public function show(Agent $agent)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(Agent $agent)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Agent $agent)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Agent $agent)
    {
        //
    }
}
