<?php

namespace App\Http\Controllers;

use App\Models\ClientTrash;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Http\Resources\ClientTrashCollection;

class ClientTrashController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $data = ClientTrash::with("client.address_client")->orderBy('id', 'asc')->get();
        if($data->count() != 0 ){
            return new ClientTrashCollection($data);
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

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(),[
            'client_id'         => 'required|int',    
            'state_trash_id'    => 'required|int'
        ]);
        if(!$validator->stopOnFirstFailure()->fails()) {
            $validated  = $validator->validated();
            $data     = ClientTrash::updateOrCreate([
                'client_id'      => $validated['client_id'],
                'state_trash_id' => $validated['state_trash_id'],
                'is_active'      => true,
            ]);
            return response()->json([
                'data' => $data,
                'message' => "On arrive pour nettoyer votre poubelle !!"
             ],201);
        }
        return response()->json([
            'message' => $validator->errors()
         ],402);
    }

    /**
     * Display the specified resource.
     */
    public function show(int $client_id)
    {
        $data = ClientTrash::where("client_id",$client_id)->with("client.address_client")->first();
        if($data){
        return response()->json([
            'message' => "",
            'data' => $data
         ],201);
        }
        else{
            return response()->json([
                'message' => "Votre poubelle n'est pas encore pris en charge cliquez sur Vidage",
             ],404);
        }
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(ClientTrash $clientTrash)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function clean(Request $request,int $id)
    {
        $validator = Validator::make($request->all(),[
            'client_id'         => 'required|int',    
            'state_trash_id'    => 'required|int'
        ]);
        $state = 2;
        if(!$validator->stopOnFirstFailure()->fails()) {
            $validated  = $validator->validated();
           if((ClientTrash::where("client_id",$validated['client_id'])->first())->state_trash_id == $state){
                $state = 1;
           }
           elseif((ClientTrash::where("client_id",$validated['client_id'])->first())->state_trash_id == 3){
            $state = 2;
           }
            $data= ClientTrash::where("id",$id)->update([
                    'client_id'      => $validated['client_id'],
                    'state_trash_id' => $state,
                    'is_active'      => true,
            ]);
            $data = ClientTrash::where("id",$id)->first();
            return response()->json([
                'data'    => $data,
                'message' => $state == 1 ?"Votre poubelle est actuellement propre":"Nettoyage de la poubelle encours"
             ],201);
        }
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(ClientTrash $clientTrash)
    {
        //
    }
}
