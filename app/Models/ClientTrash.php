<?php

namespace App\Models;

use App\Models\Client;
use Illuminate\Database\Eloquent\Model;

class ClientTrash extends Model
{
    //
    protected $fillable = [
        'client_id',
        'state_trash_id',
        'is_active'
    ];
    
    public function client(){
        return $this->belongsTo(Client::class, 'client_id' , 'id');
    }
}
