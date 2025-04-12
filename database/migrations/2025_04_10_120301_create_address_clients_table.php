<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('address_clients', function (Blueprint $table) {
            $table->id();
            $table->foreignId('client_id')->nullable()->index();
            $table->foreignId('commune_id')->nullable()->index();
            $table->text("avenue");
            $table->text("numero_parcelle")->nullable();
            $table->text("quartier");
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('address_clients');
    }
};
