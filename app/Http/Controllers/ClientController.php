<?php

namespace App\Http\Controllers;

use App\Models\Client;
use Illuminate\Http\Request;
use App\Models\AddressClient;
use App\Http\Resources\ClientCollection;
use Illuminate\Support\Facades\Validator;

class ClientController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        //
        $data = Client::with("address_client")->orderBy('id', 'asc')->get();
        if($data->count() != 0 ){
            return new ClientCollection($data);
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

    public function auth_client(Request $request){
        $validator = Validator::make($request->all(),[
            "telephone" =>'required|string',
            "avenue"=>"required|string"
        ]);
        if($validator->stopOnFirstFailure()->fails()){
            return response()->json([
                'message' => $validator->errors()
             ],402);
        }
        $field = $validator->validated();
        $client = Client::where('telephone',$field['telephone'])->with("address_client")->first();
        if(!$client){
            return response()->json([
               'message' => "Ce numéro n'est pas identifié"
            ],404);
        }
        if((AddressClient::where("client_id",$client->id)->first())->avenue == strtolower($field['avenue'])){
            return response()->json([
                'message' => 'login success',
                'client' => $client,
             ],200);
        }
        return response()->json([
            'message' => 'L\'avenue inserez n\'est pas associé à votre numéro',
         ],403);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(),[
            'nom'               => 'required|string',
            'prenom'            => 'required|string',
            'telephone'         => 'required|string',
            'commune_id'        => 'int',
            'avenue'            => "required|string",
            'quartier'          => "required|string",
            'numero_parcelle'   => "string"
        ]);
        if(!$validator->stopOnFirstFailure()->fails()) {
            $validated  = $validator->validated();
            $code       = $this->code_initial_client . count(Client::all()) + 1;
            $client     = Client::updateOrCreate([
                            'code'              => $code,
                            'nom'               => $validated['nom'],
                            'prenom'            => $validated['prenom'],
                            'telephone'         => $validated['telephone']
                        ]);
            if($client){
                $client_address = AddressClient::updateOrCreate([
                    'commune_id'      => $validated['commune_id'],
                    'client_id'       => $client->id,
                    'avenue'          => strtolower($validated["avenue"]),
                    'quartier'        => $validated["quartier"],
                    'numero_parcelle' => $validated['numero_parcelle']
                ]);
                if($client_address){
                    return response()->json([
                        "client" => $client_address,
                        "message"=>"Compte client créer avec succès",
                    ],201);
                }
            }
        }
        return response()->json([
            "message"=>$validator->errors(),
        ],400);
    }

    /**
     * Display the specified resource.
     */
    public function show(Client $client)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(Client $client)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Client $client)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Client $client)
    {
        //
    }
}
